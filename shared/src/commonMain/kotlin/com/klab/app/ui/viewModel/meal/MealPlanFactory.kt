package com.klab.app.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.klab.app.service.usecase.MealPlanService
import kotlin.reflect.KClass

class MealPlanFactory(
    private val mealPlanService: MealPlanService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
        if (modelClass == MealPlanViewModel::class) {
            @Suppress("UNCHECKED_CAST")
            return MealPlanViewModel(mealPlanService) as T
        }

        throw IllegalArgumentException("ViewModel desconhecido")
    }
}