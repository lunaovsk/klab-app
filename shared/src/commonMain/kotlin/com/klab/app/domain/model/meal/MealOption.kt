package com.klab.app.domain.model.meal

import com.klab.app.domain.model.food.Macros

class MealOption(
    val id: Long = 0L,
    title: String,
    displayOrder: Int,
    val items: MutableSet<MealItem> = mutableSetOf()
) {
    var title: String = title
        private set

    var displayOrder: Int = displayOrder
        private set

    init {
        require(title.isNotBlank()) { "Option title must be non-blank" }
        require(displayOrder > 0) { "Option displayOrder must be positive" }
    }

    fun addItem(item: MealItem): MealOption {
        require(this.items.none { it.foodId == item.foodId }) {
            "Food already exists in this option"
        }
        this.items.add(item)
        return this
    }

    fun removeItem(itemId: Long): MealOption {
        val removed = this.items.removeAll { it.id == itemId }
        require(removed) { "Item not found with id: $itemId" }
        return this
    }

    fun calculateMacros(): Macros {
        return this.items
            .map { it.calculateMacros() }
            .fold(Macros(0f, 0f, 0f, 0f)) { acc, macros -> acc.sumNutrients(macros) }
    }

    fun update(newTitle: String? = null, newDisplayOrder: Int? = null) {
        if (!newTitle.isNullOrBlank()) {
            this.title = newTitle.trim()
        }
        if (newDisplayOrder != null) {
            require(newDisplayOrder > 0) { "Option displayOrder must be positive" }
            this.displayOrder = newDisplayOrder
        }
    }
}
