package com.klab.app.ui.viewModel.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.klab.app.domain.dto.MacrosDTO
import com.klab.app.domain.dto.request.MealPlanRequest
import com.klab.app.domain.dto.request.MealPlanUpdateRequest
import com.klab.app.domain.dto.response.MealPlanResponse
import com.klab.app.service.usecase.MealPlanService
import com.klab.app.ui.screens.enums.PlanPeriodFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock

class MealPlanViewModel( private val mealPlanService: MealPlanService): ViewModel() {

    private val changeUiState = MutableStateFlow(MealPlanState())
    val uiState: StateFlow<MealPlanState> = changeUiState.asStateFlow()

    fun previewMacros(request: MealPlanRequest): MacrosDTO {
        return mealPlanService.previewMacros(request)
    }

    fun saveMealPlan(request: MealPlanRequest) {

        if (changeUiState.value.isLoading) return

        val currentPlanId = changeUiState.value.currentPlanId

        changeUiState.update {
            it.copy(
                isLoading = true,
                error = null,
                savedPlan = null
            )
        }

        viewModelScope.launch {
            try {

                val plan = if (currentPlanId == null) {
                    mealPlanService.create(request)
                } else {
                    updateExistingPlan(currentPlanId, request)
                }

                changeUiState.update {
                    it.copy(
                        currentPlanId = plan.id,
                        savedPlan = plan
                    )
                }

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {
                changeUiState.update {
                    it.copy(
                        error = exception.message
                            ?: "Erro ao salvar planejamento."
                    )
                }

            } finally {
                changeUiState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }    fun consumeSavedPlan() {
        changeUiState.update {
            it.copy(savedPlan = null)
        }
    }    fun loadPlans() {

        if (changeUiState.value.isLoading) return

        viewModelScope.launch {

            changeUiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {
                val plans = mealPlanService.findAllActive()
                    .sortedWith(
                        compareByDescending<
                                com.klab.app.domain.dto.response.MealPlanResponse
                                > { it.createdAt }
                            .thenByDescending { it.id }
                    )

                changeUiState.update {
                    applyFilters(
                        it.copy(
                            plans = plans,
                            isLoading = false
                        )
                    )
                }

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {
                changeUiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.message
                            ?: "Erro ao carregar planejamentos."
                    )
                }
            }
        }
    }

    fun onSearchChange(query: String) {
        changeUiState.update {
            applyFilters(it.copy(searchQuery = query))
        }
    }

    fun onFilterChange(filter: PlanPeriodFilter) {
        changeUiState.update {
            applyFilters(it.copy(selectedFilter = filter))
        }
    }

    private fun applyFilters(
        state: MealPlanState
    ): MealPlanState {

        val today = Clock.System.todayIn(
            TimeZone.currentSystemDefault()
        )

        val startOfWeek = today.minus(
            today.dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY
        )

        val endOfWeek = startOfWeek.plus(6, DateTimeUnit.DAY)

        val filteredPlans = state.plans.filter { plan ->

            val matchesSearch = plan.title.contains(
                state.searchQuery.trim(),
                ignoreCase = true
            )

            val matchesPeriod = when (state.selectedFilter) {

                PlanPeriodFilter.ALL -> true

                PlanPeriodFilter.TODAY ->
                    plan.createdAt == today

                PlanPeriodFilter.WEEK ->
                    plan.createdAt in startOfWeek..endOfWeek

                PlanPeriodFilter.MONTH ->
                    plan.createdAt.year == today.year &&
                            plan.createdAt.month == today.month
            }

            matchesSearch && matchesPeriod
        }

        return state.copy(filteredPlans = filteredPlans)
    }

    private suspend fun updateExistingPlan(
        planId: Long,
        request: MealPlanRequest
    ): MealPlanResponse {

        val updateRequest = MealPlanUpdateRequest(
            id = planId,
            title = request.title,
            calories = request.calories,
            carbosPerc = request.carbosPerc,
            proteinPerc = request.proteinPerc,
            fatPerc = request.fatPerc
        )

        return mealPlanService.update(updateRequest)
    }

    // funcoes para recuperar valores

    fun onTitleChange(value: String) {
        changeUiState.update {
            it.copy(planTitle = value)
        }
    }

    fun onCaloriesChange(value: String) {
        changeUiState.update {
            it.copy(caloriesInput = value)
        }
    }

    fun onCarbohydratesChange(value: String) {
        changeUiState.update {
            it.copy(carbohydratesInput = value)
        }
    }

    fun onProteinsChange(value: String) {
        changeUiState.update {
            it.copy(proteinsInput = value)
        }
    }

    fun onLipidsChange(value: String) {
        changeUiState.update {
            it.copy(lipidsInput = value)
        }
    }

    companion object {
        fun factory(mealPlanService: MealPlanService): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MealPlanViewModel(
                        mealPlanService = mealPlanService
                    )
                }
            }
    }
}