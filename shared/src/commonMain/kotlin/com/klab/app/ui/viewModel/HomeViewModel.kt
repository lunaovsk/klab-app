package com.klab.app.ui.viewModel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.klab.app.service.usecase.MealPlanService
import com.klab.app.ui.viewModel.HomeState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mealPlanService: MealPlanService
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    fun loadPlans() {
        viewModelScope.launch {

            _uiState.update {
                it.copy(isLoading = true, error = null)
            }

            try {
                val plans = mealPlanService.findAllActive()
                    .sortedByDescending { it.id }

                _uiState.update {
                    it.copy(plans = plans, isLoading = false)
                }

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false,
                        error = e.message ?: "Erro ao carregar planejamentos."
                    )
                }
            }
        }
    }

    companion object {
        fun factory(mealPlanService: MealPlanService) =
            viewModelFactory {
                initializer { HomeViewModel(mealPlanService) }
            }
    }
}