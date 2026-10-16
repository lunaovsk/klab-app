package com.klab.app.domain.model.meal

class MealOption (
    val id: Long,
    val title: String,
    val displayOrder: Int,
    val items: MutableSet<MealItem>


) {

    init {
        require(title.isNotBlank())
        require(displayOrder > 0)
    }

}