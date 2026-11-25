
package com.klab.app.ui.viewModel.food

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.klab.app.domain.model.food.Food
import com.klab.app.service.usecase.FoodService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(
    FlowPreview::class,
    ExperimentalCoroutinesApi::class
)
class FoodSearchViewModel( private val foodService: FoodService) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _groups = MutableStateFlow<List<String>>(emptyList())
    val groups = _groups.asStateFlow()

    private val _selectedGroup = MutableStateFlow<String?>(null)
    val selectedGroup = _selectedGroup.asStateFlow()

    val foods: Flow<PagingData<Food>> = combine(
        _query
            .debounce(300)
            .map { it.trim() }
            .distinctUntilChanged(),
        _selectedGroup
    ) { query, group ->
        query to group
    }
        .distinctUntilChanged()
        .flatMapLatest { (query, group) ->
            when {
                group != null && query.isNotBlank() -> {
                    foodService.searchDescriptionByGroup(query, group)
                }

                group != null -> {
                    foodService.getByGroup(group)
                }

                query.isNotBlank() -> {
                    foodService.search(query)
                }

                else -> {
                    flowOf(PagingData.empty())
                }
            }
        }
        .cachedIn(viewModelScope)


    init { loadGroups() }

    private fun loadGroups() {
        viewModelScope.launch {
            try {
                _groups.value = foodService.listAllGroups()
            } catch (e: Exception) {
                _groups.value = emptyList()
            }
        }
    }

    fun onSearchChange(value: String) {
        _query.value = value
    }

    fun onGroupChange(group: String?) {
        _selectedGroup.value = group
    }

    companion object {
        fun factory(foodService: FoodService): ViewModelProvider.Factory = viewModelFactory {
            initializer { FoodSearchViewModel(foodService) }
        }
    }
}
