package com.klab.app.domain.model.meal

class MealOption (
    val id: Long = 0L,
    val title: String,
    val displayOrder: Int,
    val items: MutableSet<MealItem> = mutableSetOf()
) {

    init {
        require(title.isNotBlank()) { throw IllegalArgumentException("Option title must be non-blank") }
        require(displayOrder > 0) { throw IllegalArgumentException("Option displayOrder must be positive") }
    }

    fun addItem(item: MealItem): MealItem {
        val existingItem = this.items.find { it.foodId == item.foodId }
        return if (existingItem != null) {
            val summedGrams = GramsAmount(existingItem.amountInGrams.amount + item.amountInGrams.amount)
            val updatedItem = existingItem.defineAmountInGrams(summedGrams)
            this.items.remove(existingItem)
            this.items.add(updatedItem)
            updatedItem
        } else {
            this.items.add(item)
            item
        }
    }

    fun removeItem(itemId: Long): MealOption {
        val removed = this.items.removeAll { it.id == itemId }
        require(removed) { throw IllegalArgumentException("Item not found with id: $itemId") }
        return this
    }

    fun update(title: String? = null, displayOrder: Int? = null, items: MutableSet<MealItem>? = null): MealOption {
        return MealOption(
            id = this.id,
            title = if (!title.isNullOrBlank()) title.trim() else this.title,
            displayOrder = displayOrder ?: this.displayOrder,
            items = items ?: this.items
        )
    }
}
