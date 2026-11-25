package com.klab.app.ui.viewModel.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.initializer
import com.klab.app.service.usecase.MealPlanService
import com.klab.app.service.usecase.MealService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlanningViewModel(private val mealPlanService: MealPlanService, private val mealService: MealService): ViewModel() {

    private val changeUiState = MutableStateFlow(PlanningState())
    val uiState = changeUiState.asStateFlow()

    fun loadMeals(planId: Long) {
        if (changeUiState.value.isLoading) return

        viewModelScope.launch {
            changeUiState.update { it.copy(isLoading = true, error = null) }
            try {
                val meals = mealService.getMealsByPlanId(planId)

                changeUiState.update { it.copy(isLoading = false, meals = meals) }

            } catch (exception: Exception) {
                changeUiState.update { it.copy(isLoading = false,
                    error = exception.message ?: "Failed to load meals")
                }
            }
        }
    }
    fun loadPlan(planId: Long) {
        viewModelScope.launch {
            try {
                val plan = mealPlanService.findById(planId)

                changeUiState.value = changeUiState.value.copy(
                    planName = plan.title,
                    dailyCalories = plan.goal.calorie
                )
            } catch (e: Exception) {
                changeUiState.value = changeUiState.value.copy(
                    error = e.message
                        ?: "Erro ao carregar planejamento."
                )
            }
        }
    }


    companion object {
        fun factory(mealPlanService: MealPlanService, mealService: MealService): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { PlanningViewModel(mealPlanService, mealService) }
            }
    }
}