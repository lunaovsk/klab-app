package com.klab.app.di

import androidx.room.RoomDatabase
import com.klab.app.domain.dto.meal.MealDistribRequest
import com.klab.app.domain.dto.meal.MealPlanRequest
import com.klab.app.domain.repository.FoodRepository
import com.klab.app.domain.repository.MealItemRepository
import com.klab.app.domain.repository.MealOptionRepository
import com.klab.app.domain.repository.MealPlanRepository
import com.klab.app.domain.repository.MealRepository
import com.klab.app.domain.validation.DailyGoalsPlan
import com.klab.app.domain.validation.FoodEmptyTrim
import com.klab.app.domain.validation.FoodValidate
import com.klab.app.domain.validation.MealDistribValidate
import com.klab.app.domain.validation.MealEditorValidate
import com.klab.app.domain.validation.MealGoalValidate
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
    single<MealPlanValidate<List<Int>>>(named("percentageValidator")) { PercentageValidate() }
    single<MealPlanValidate<MealPlanRequest>>(named("dailyGoalsValidator")) { DailyGoalsPlan() }
    single<MealPlanValidate<List<MealDistribRequest>>>(named("distributionValidator")) { MealDistribValidate() }
    single { MealGoalValidate() }
    single { MealEditorValidate() }
    single { PlanningPdfBuilder() }

    single { FoodService(get(), get()) }
    single {
        MealPlanService(
            mealPlanRepository = get(),
            dailyGoalsValidator = get(named("dailyGoalsValidator")),
            percentageValidator = get(named("percentageValidator"))
        )
    }
    single {
        MealService(
            mealRepository = get(),
            mealPlanRepository = get(),
            distributionValidator = get(named("distributionValidator")),
            percentageValidator = get(named("percentageValidator"))
        )
    }
    single {
        MealOptionService(
            get(), get(), get(),
            get(), get(), get()
        )
    }
    single { PlanningPdfService(get(), get(), get(), get()) }
}
