package com.klab.app.ui.viewModel.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.klab.app.domain.dto.request.MealItemRequest
import com.klab.app.domain.dto.request.MealOptionRequest
import com.klab.app.domain.model.food.Food
import com.klab.app.service.usecase.MealItemService
import com.klab.app.service.usecase.MealOptionService
import com.klab.app.service.usecase.MealService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MealEditorViewModel(
    private val mealService: MealService,
    private val mealOptionService: MealOptionService,
    private val mealItemService: MealItemService
) : ViewModel() {

    private val _uiState = MutableStateFlow(MealEditorState())
    val uiState = _uiState.asStateFlow()

    private var nextDraftId = -1L
    private var currentMealId: Long? = null
    private val foodCache = mutableMapOf<Long, Food>()

    fun load(mealId: Long) {
        if (_uiState.value.isSaving) return

        if (currentMealId != mealId) {
            nextDraftId = -1L
            currentMealId = mealId
            _uiState.value = MealEditorState()
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {
                val data = mealService.findEditorDataById(mealId)

                if (currentMealId == mealId) {
                    _uiState.value = _uiState.value.copy(
                        data = data,
                        isLoading = false
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (currentMealId == mealId) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Erro ao carregar refeição."
                    )
                }
            }
        }
    }

    fun addOptionDraft() {
        val state = _uiState.value

        if (state.isLoading || state.isSaving || state.data == null) {
            return
        }

        val draftId = nextDraftId--

        _uiState.value = state.copy(
            drafts = state.drafts + (draftId to emptyList<Pair<Long, Float>>()),
            error = null
        )
    }

    fun addFoodsToDraft(
        mealId: Long,
        draftId: Long,
        foods: List<Pair<Food, Float>>
    ) {
        foods.forEach { (food, _) ->
            foodCache[food.id] = food
        }

        updateDraft(mealId, draftId) { items ->
            foods.fold(items) { current, (food, grams) ->
                addOrUpdateFood(current, food.id, grams)
            }
        }
    }

    fun updateDraftFood(
        mealId: Long,
        draftId: Long,
        foodId: Long,
        grams: Float
    ) {
        updateDraft(mealId, draftId) { items ->
            addOrUpdateFood(items, foodId, grams)
        }
    }

    fun removeDraftFood(
        mealId: Long,
        draftId: Long,
        foodId: Long
    ) {
        updateDraft(mealId, draftId) { items ->
            items.filterNot { it.first == foodId }
        }
    }

    fun removeDraft(draftId: Long) {
        val state = _uiState.value
        if (state.isSaving) return

        _uiState.value = state.copy(
            drafts = state.drafts.filterNot { it.first == draftId },
            draftPreviews = state.draftPreviews - draftId,
            previewLoadingIds = state.previewLoadingIds - draftId,
            error = null
        )
    }

    private fun addOrUpdateFood(
        items: List<Pair<Long, Float>>,
        foodId: Long,
        grams: Float
    ): List<Pair<Long, Float>> {
        return if (items.any { it.first == foodId }) {
            items.map { item ->
                if (item.first == foodId) foodId to grams else item
            }
        } else {
            items + (foodId to grams)
        }
    }

    private fun updateDraft(
        mealId: Long,
        draftId: Long,
        transform: (List<Pair<Long, Float>>) -> List<Pair<Long, Float>>
    ) {
        val state = _uiState.value
        if (state.isSaving || currentMealId != mealId) return

        val currentItems = state.drafts.firstOrNull { it.first == draftId }?.second ?: return
        val updatedItems = transform(currentItems)

        val preview = try {
            state.data?.meal?.let { currentMeal ->
                val draftFoods = updatedItems.map { (foodId, grams) ->
                    val food = requireNotNull(foodCache[foodId]) {
                        "Food not found for meal item $foodId."
                    }
                    food to grams
                }
                mealOptionService.previewDraft(
                    draftId = draftId,
                    meal = currentMeal,
                    items = draftFoods
                )
            }
        } catch (exception: Exception) {
            _uiState.value = state.copy(
                error = exception.message ?: "Erro ao calcular opção."
            )
            return
        }

        val updatedPreviews = if (preview != null) {
            state.draftPreviews + (draftId to preview)
        } else {
            state.draftPreviews - draftId
        }

        _uiState.value = state.copy(
            drafts = state.drafts.map { (id, items) ->
                if (id == draftId) id to updatedItems else id to items
            },
            draftPreviews = updatedPreviews,
            previewLoadingIds = state.previewLoadingIds - draftId,
            error = null
        )
    }

    fun saveAllDrafts(mealId: Long) {
        val state = _uiState.value
        if (currentMealId != mealId || !state.canSave) return

        val draftsToSave = state.drafts.map { (_, items) -> items.toList() }
        val firstOrder = (state.data?.options?.maxOfOrNull { it.displayOrder } ?: 0) + 1

        _uiState.value = state.copy(
            isSaving = true,
            error = null
        )

        viewModelScope.launch {
            try {
                draftsToSave.forEachIndexed { index, draftItems ->
                    val savedOption = mealOptionService.create(
                        MealOptionRequest(
                            mealId = mealId,
                            title = "Opção ${firstOrder + index}",
                            displayOrder = firstOrder + index
                        )
                    )
                    draftItems.forEach { (foodId, grams) ->
                        mealItemService.create(
                            MealItemRequest(
                                optionId = savedOption.id,
                                foodId = foodId,
                                amountInGrams = grams
                            )
                        )
                    }
                }
            } catch (e: CancellationException) {
                _uiState.value = _uiState.value.copy(isSaving = false)
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = e.message ?: "Erro ao salvar opções."
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                drafts = emptyList(),
                draftPreviews = emptyMap(),
                previewLoadingIds = emptySet()
            )

            try {
                val updatedData = mealService.findEditorDataById(mealId)
                _uiState.value = _uiState.value.copy(
                    data = updatedData,
                    isSaving = false
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = "Opções salvas, mas não foi possível atualizar a tela."
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    companion object {
        fun factory(
            mealService: MealService,
            mealOptionService: MealOptionService,
            mealItemService: MealItemService
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MealEditorViewModel(mealService, mealOptionService, mealItemService)
            }
        }
    }
}
