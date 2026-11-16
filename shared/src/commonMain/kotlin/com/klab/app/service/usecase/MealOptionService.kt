package com.klab.app.service.usecase

import com.klab.app.domain.dto.meal.*
import com.klab.app.domain.model.food.Macros
import com.klab.app.domain.model.meal.GramsAmount
import com.klab.app.domain.model.meal.MealItem
import com.klab.app.domain.model.meal.MealOption
import com.klab.app.domain.repository.MealRepository
import com.klab.app.domain.repository.MealOptionRepository
import com.klab.app.domain.repository.MealItemRepository
import com.klab.app.domain.validation.MealEditorValidate
import com.klab.app.domain.validation.MealGoalValidate
import com.klab.app.infra.mapper.toDTO
import com.klab.app.infra.mapper.toResponse
import org.koin.core.component.getScopeId

class MealOptionService(
    private val mealRepository: MealRepository,
    private val optionRepository: MealOptionRepository,
    private val itemRepository: MealItemRepository,
    private val foodService: FoodService,
    private val mealGoalValidator: MealGoalValidate,
    private val mealEditorValidator: MealEditorValidate
) {

    suspend fun getMealEditor(mealId: Long): MealEditorData {

        val meal = mealRepository.findById(mealId)
            ?: throw NoSuchElementException(
                "Refeição não encontrada."
            )

        val options = optionRepository.findByMealId(mealId)
            .sortedBy { it.displayOrder }
            .map { option ->

                val details = getOptionItems(option.id)

                val total = sumMacros(
                    details.map {
                        it.food.amountPer100g.calculateForPortion(
                            it.item.amountInGrams
                        )
                    }
                )

                MealEditorOption(
                    id = option.id,
                    title = option.title,
                    displayOrder = option.displayOrder,
                    items = details,
                    total = total.toDTO(),
                    isWithinGoal = mealGoalValidator.isWithinGoal(
                        total = total,
                        goal = meal.goal
                    )
                )
            }

        return MealEditorData(
            meal = meal.toResponse(),
            options = options
        )
    }

    suspend fun previewDraft(mealId: Long, draftId: Long, items: List<Pair<Long, Float>>): MealEditorOption {

        val meal = mealRepository.findById(mealId)
            ?: throw NoSuchElementException("Refeição não encontrada.")

        mealEditorValidator.validateUniqueFoods(items.map { it.first })

        val details = getDraftItems(items)

        val total = calculateTotal(details)

        return MealEditorOption(
            id = draftId,
            title = "Rascunho",
            displayOrder = 0,
            items = details,
            total = total.toDTO(),
            isWithinGoal = items.isNotEmpty() &&
                    mealGoalValidator.isWithinGoal(total = total, goal = meal.goal)
        )
    }

    suspend fun saveCompletedOptions(mealId: Long, drafts: List<List<Pair<Long, Float>>>): List<Long> {

        val meal = mealRepository.findById(mealId)
            ?: throw NoSuchElementException("Refeição não encontrada.")

        if (drafts.isEmpty()) {
            throw IllegalArgumentException("Adicione pelo menos uma opção.")
        }

        val validatedItems = drafts.map { draft ->

            if (draft.isEmpty()) {
                throw IllegalArgumentException("Todas as opções precisam conter alimentos.")
            }

            mealEditorValidator.validateUniqueFoods(draft.map { it.first })

            val details = getDraftItems(draft)

            val total = calculateTotal(details)

            mealGoalValidator.validateCompletedOption(total = total, goal = meal.goal)

            details.map { details ->
                MealItem(id = 0L, foodId = details.item.foodId, amountInGrams = GramsAmount(details.item.amountInGrams))
            }
        }

        val existingOptions = optionRepository.findByMealId(mealId)

        val firstOrder = (existingOptions.maxOfOrNull { it.displayOrder } ?: 0) + 1

        val options = validatedItems.mapIndexed { index, items ->

            val order = firstOrder + index

            MealOption(id = 0L, title = "Opção $order", displayOrder = order, items = items.toMutableSet())
        }

        return optionRepository.insertAll(mealId = mealId, options = options)
    }

    suspend fun removeOption(mealId: Long, optionId: Long) {
        val options = optionRepository.findByMealId(mealId)

        mealEditorValidator.validateOptionBelongsToMeal(
            optionId = optionId,
            mealOptionIds = options.map { it.id }
        )

        optionRepository.deleteById(optionId)
    }

    private suspend fun getOptionItems(optionId: Long): List<MealItemDetailsResponse> {
        val items = itemRepository.findByOptionId(optionId)

        return items.map { item ->
            MealItemDetailsResponse(
                item = item.toResponse(),
                food = foodService.getById(item.foodId)
            )
        }
    }

    private suspend fun getDraftItems(items: List<Pair<Long, Float>>): List<MealItemDetailsResponse> {

        return items.map { (foodId, grams) ->
            mealEditorValidator.validateGrams(grams)
            val food = foodService.getById(foodId)
            MealItemDetailsResponse(
                item = MealItemResponse(id = 0L, foodId = foodId, amountInGrams = grams),
                food = food
            )
        }
    }

    private fun calculateTotal( details: List<MealItemDetailsResponse>): Macros {

        val portions = details.map { details ->
            details.food.amountPer100g.calculateForPortion(details.item.amountInGrams)
        }
        return sumMacros(portions)
    }

    private fun sumMacros(macros: List<Macros>): Macros {

        return Macros(
            calorie = macros.sumOf { it.calorie.toDouble() }.toFloat(),
            carb = macros.sumOf { it.carb.toDouble() }.toFloat(),
            protein = macros.sumOf { it.protein.toDouble() }.toFloat(),
            fat = macros.sumOf { it.fat.toDouble() }.toFloat()
        )
    }
}
