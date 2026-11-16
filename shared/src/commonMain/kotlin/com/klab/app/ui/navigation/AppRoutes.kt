package com.klab.app.ui.navigation

object AppRoutes {
    const val HOME = "home"
    const val DAILY_GOALS= "daily_goals"
    const val MEAL_DISTRI="meal_distribution/{planId}"
    const val PLANNING_OVERVIEW = "planning/{planId}"
    const val MEAL_EDITOR = "meal_editor/{mealId}"
    const val SEARCH_FOOD = "search_food"
    const val SEARCH_FOOD_OPTION = "search_food/{mealId}/{optionId}"
    const val PDF_PREVIEW = "pdf_preview/{planId}"

    fun mealDistribution(planId: Long) = "meal_distribution/$planId"
    fun planningOverview(planId: Long) = "planning/$planId"
    fun mealEditor(mealId: Long) = "meal_editor/$mealId"
    fun searchFoodOption(mealId: Long, optionId: Long) = "search_food/$mealId/$optionId"
    fun pdfPreview(planId: Long): String { return "pdf_preview/$planId" }
}
