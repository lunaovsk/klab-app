package com.klab.app.di

import androidx.room.RoomDatabase
import com.klab.app.domain.repository.FoodRepository
import com.klab.app.domain.repository.MealPlanRepository
import com.klab.app.domain.validation.FoodEmptyTrim
import com.klab.app.domain.validation.FoodValidate
import com.klab.app.infra.database.KlabDatabase
import com.klab.app.infra.database.getRoomDatabase
import com.klab.app.infra.repository.FoodRepositoryImpl
import com.klab.app.infra.repository.MealPlanRepositoryImpl
import com.klab.app.service.usecase.FoodService
import com.klab.app.service.usecase.MealPlanService
import org.koin.dsl.module

var appDatabaseBuilder: RoomDatabase.Builder<KlabDatabase>? = null

fun appModule(databaseBuilder: RoomDatabase.Builder<KlabDatabase>) = module {
    single<KlabDatabase> { getRoomDatabase(databaseBuilder) }

    single { get<KlabDatabase>().foodDao() }
    single { get<KlabDatabase>().mealPlanDao() }

    single<FoodRepository> { FoodRepositoryImpl(get()) }
    single<MealPlanRepository> { MealPlanRepositoryImpl(get()) }

    single<FoodValidate<String>> { FoodEmptyTrim() }

    single { FoodService(get(), get()) }
    single { MealPlanService(get()) }
}
