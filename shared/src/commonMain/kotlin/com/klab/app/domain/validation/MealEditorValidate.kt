
package com.klab.app.domain.validation

class MealEditorValidate {

    fun validateGrams(grams: Float) {
        if (!grams.isFinite() || grams <= 0f) {
            throw IllegalArgumentException(
                "Informe uma quantidade válida em gramas."
            )
        }
    }

    fun validateOptionBelongsToMeal(
        optionId: Long,
        mealOptionIds: Collection<Long>
    ) {
        if (optionId !in mealOptionIds) {
            throw IllegalArgumentException(
                "A opção não pertence a esta refeição."
            )
        }
    }

    fun validateItemBelongsToOption(itemId: Long, optionItemIds: Collection<Long>) {
        if (itemId !in optionItemIds) {
            throw IllegalArgumentException(
                "O alimento não pertence a esta opção."
            )
        }
    }

    fun validateUniqueFoods(foodIds: List<Long>) {
        if (foodIds.size != foodIds.distinct().size) {
            throw IllegalArgumentException(
                "Uma opção não pode conter o mesmo alimento duas vezes."
            )
        }
    }

}
