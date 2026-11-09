package com.klab.app.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.klab.app.domain.dto.meal.MealDistribRequest
import com.klab.app.service.usecase.MealService
import com.klab.app.ui.viewModel.meal.MealDistriState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MealDistributionViewModel(
    private val mealService: MealService
) : ViewModel() {

    private val changeUiState = MutableStateFlow(MealDistriState())
    val uiState: StateFlow<MealDistriState> = changeUiState.asStateFlow()

    fun previewMacros(planId: Long, request: MealDistribRequest, index: Int) {
        viewModelScope.launch {
            val macros = mealService.previewMacros(planId = planId, request = request)
            changeUiState.update { state -> state.copy(mealMacros = state.mealMacros + (index to macros)) }
        }
    }

    fun createMeals(planId: Long, requests: List<MealDistribRequest>) {
        if (changeUiState.value.isLoading) return

        viewModelScope.launch {

            changeUiState.value = MealDistriState(isLoading = true)

            try {
                val mealIds = mealService.createMeals(planId, requests)

                changeUiState.value = MealDistriState(createdMeals = mealIds)

            } catch (exception: Exception) {
                changeUiState.value = MealDistriState(error = exception.message ?: "Erro ao criar as refeições.")
            }
        }
    }

    fun consumeCreatedMeals() {
        changeUiState.value = changeUiState.value.copy(createdMeals = null)
    }


    companion object {
        fun factory(mealService: MealService): ViewModelProvider.Factory = viewModelFactory {
                initializer {
                    MealDistributionViewModel(mealService = mealService)
                }
            }
    }
}