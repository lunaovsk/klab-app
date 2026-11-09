package com.klab.app.ui.viewModel.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.initializer
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.klab.app.domain.model.food.Food
import com.klab.app.service.usecase.FoodService
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
class FoodSearchViewModel(
    private val foodService: FoodService
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val query = searchQuery.asStateFlow()

    val foods = searchQuery
        .debounce(10)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(PagingData.empty<Food>())
            } else {
                foodService.search(query)
            }
        }
        .cachedIn(viewModelScope)

    fun onSearchChange(value: String) {
        searchQuery.value = value
    }

    companion object {
        fun factory(foodService: FoodService): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { FoodSearchViewModel(foodService) }
            }
    }
}