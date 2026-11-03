package com.klab.app.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.klab.app.domain.dto.meal.MealPlanRequest
import com.klab.app.domain.model.food.Macros
import com.klab.app.service.usecase.MealPlanService
import com.klab.app.ui.viewModel.meal.MealPlanState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MealPlanViewModel( private val mealPlanService: MealPlanService): ViewModel() {

    private val changeUiState = MutableStateFlow(MealPlanState())
    val uiState: StateFlow<MealPlanState> = changeUiState.asStateFlow()

    fun previewMacros(request: MealPlanRequest): Macros {
        return mealPlanService.previewMacros(request)
    }

    fun createMealPlan(request: MealPlanRequest) {
        viewModelScope.launch {
            changeUiState.value = MealPlanState(isLoading = true)

            try {
                val plan = mealPlanService.create(request)

                changeUiState.value = MealPlanState(createdPlan = plan)

            } catch (exception: Exception) {
                changeUiState.value = MealPlanState(
                    error = exception.message ?: "Erro ao criar planejamento."
                )
            }
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