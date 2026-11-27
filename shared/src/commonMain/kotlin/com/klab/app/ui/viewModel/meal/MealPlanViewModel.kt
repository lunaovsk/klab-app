package com.klab.app.ui.viewModel.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.klab.app.domain.dto.MacrosDTO
import com.klab.app.domain.dto.request.MealPlanRequest
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

    fun createMealPlan(request: MealPlanRequest) {
        if (changeUiState.value.isLoading) return
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
    fun consumeCreatedPlan() {
        changeUiState.value = changeUiState.value.copy(createdPlan = null)
    }
    fun loadPlans() {

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