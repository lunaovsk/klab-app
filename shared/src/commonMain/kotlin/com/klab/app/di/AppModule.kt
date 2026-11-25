package com.klab.app.di

import androidx.room.RoomDatabase
import com.klab.app.domain.dto.request.MealPlanUpdateRequest
import com.klab.app.domain.dto.response.MealPlanResponse
import com.klab.app.domain.model.food.Food
import com.klab.app.domain.model.meal.Meal
import com.klab.app.domain.model.meal.MealItem
import com.klab.app.domain.model.meal.MealOption
import com.klab.app.domain.model.meal.MealPlan
import com.klab.app.domain.repository.FoodRepository
import com.klab.app.domain.repository.MealItemRepository
import com.klab.app.domain.repository.MealOptionRepository
import com.klab.app.domain.repository.MealPlanRepository
import com.klab.app.domain.repository.MealRepository
import com.klab.app.domain.validation.EntityExistsValidate
import com.klab.app.domain.validation.EntityValidate
import com.klab.app.domain.validation.FoodEmptyTrim
import com.klab.app.domain.validation.FoodValidate
import com.klab.app.domain.validation.MealPlanPdfValidate
import com.klab.app.domain.validation.MealPlanUpdateValidate
import com.klab.app.domain.validation.MealPlanValidate
import com.klab.app.domain.validation.PercentageValidate
import com.klab.app.infra.database.KlabDatabase
import com.klab.app.infra.database.getRoomDatabase
import com.klab.app.infra.pdf.PlanningPdfBuilder
import com.klab.app.infra.repository.FoodRepositoryImpl
import com.klab.app.infra.repository.MealItemRepositoryImpl
import com.klab.app.infra.repository.MealOptionRepositoryImpl
import com.klab.app.infra.repository.MealPlanRepositoryImpl
import com.klab.app.infra.repository.MealRepositoryImpl
import com.klab.app.service.usecase.FoodService
import com.klab.app.service.usecase.MealItemService
import com.klab.app.service.usecase.MealOptionService
import com.klab.app.service.usecase.MealPlanService
import com.klab.app.service.usecase.MealService
import com.klab.app.service.usecase.PlanningPdfService
import org.koin.core.qualifier.named
import org.koin.dsl.module

var appDatabaseBuilder: RoomDatabase.Builder<KlabDatabase>? = null

fun appModule(databaseBuilder: RoomDatabase.Builder<KlabDatabase>) = module {
    single<KlabDatabase> { getRoomDatabase(databaseBuilder) }

    single { get<KlabDatabase>().foodDao() }
    single { get<KlabDatabase>().mealPlanDao() }
    single { get<KlabDatabase>().mealDao() }
    single { get<KlabDatabase>().mealOptionDao() }
    single { get<KlabDatabase>().mealItemDao() }

    single<MealOptionRepository> { MealOptionRepositoryImpl(get()) }
    single<MealItemRepository> { MealItemRepositoryImpl(get()) }
    single<FoodRepository> { FoodRepositoryImpl(get()) }
    single<MealPlanRepository> { MealPlanRepositoryImpl(get()) }
    single<MealRepository> { MealRepositoryImpl(get()) }

    single<FoodValidate<String>> { FoodEmptyTrim() }
    single<MealPlanValidate<List<Int>>>(named("percentageValidate")) { PercentageValidate() }
    single<MealPlanValidate<MealPlanUpdateRequest>>(named("planUpdateValidate")) {
        MealPlanUpdateValidate(get(named("percentageValidate")))
    }
    single<MealPlanValidate<MealPlanResponse>>(named("planPdfValidate")) { MealPlanPdfValidate() }
    single<EntityValidate<MealPlan>>(named("planExists")) { EntityExistsValidate("MealPlan") }
    single<EntityValidate<Meal>>(named("mealExists")) { EntityExistsValidate("Meal") }
    single<EntityValidate<MealOption>>(named("optionExists")) { EntityExistsValidate("MealOption") }
    single<EntityValidate<MealItem>>(named("itemExists")) { EntityExistsValidate("MealItem") }
    single<EntityValidate<Food>>(named("foodExists")) { EntityExistsValidate("Food") }
    single { PlanningPdfBuilder() }

    single { FoodService(get(), get(), get(named("foodExists"))) }
    single {
        MealPlanService(
            mealPlanRepository = get(),
            percentageValidator = get(named("percentageValidate")),
            updateValidator = get(named("planUpdateValidate")),
            planExistsValidator = get(named("planExists"))
        )
    }
    single {
        MealService(
            mealRepository = get(),
            mealPlanRepository = get(),
            percentageValidator = get(named("percentageValidate")),
            planExistsValidator = get(named("planExists")),
            mealExistsValidator = get(named("mealExists"))
        )
    }
    single {
        MealOptionService(
            optionRepository = get(),
            mealRepository = get(),
            mealExistsValidator = get(named("mealExists")),
            optionExistsValidator = get(named("optionExists"))
        )
    }
    single {
        MealItemService(
            mealItemRepository = get(),
            mealOptionRepository = get(),
            foodRepository = get(),
            optionExistsValidator = get(named("optionExists")),
            itemExistsValidator = get(named("itemExists")),
            foodExistsValidator = get(named("foodExists"))
        )
    }
    single { PlanningPdfService(get(), get(named("planPdfValidate")), get()) }
}
