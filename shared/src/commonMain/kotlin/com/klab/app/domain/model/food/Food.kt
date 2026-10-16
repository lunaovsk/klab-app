package com.klab.app.domain.model.food

import kotlin.uuid.Uuid


class Food (
    val uid: Uuid,
    val description: String,
    val group: String,
    val amountPer100g: Macros
) {
}