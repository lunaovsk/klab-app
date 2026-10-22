package com.klab.app.infra.repository

import com.klab.app.domain.model.meal.Meal
import com.klab.app.domain.repository.MealRepository
import com.klab.app.infra.dao.MealDao
import com.klab.app.infra.local.MealEntity
import com.klab.app.infra.mapper.toDomain
import com.klab.app.infra.mapper.toEntity

class MealRepositoryImpl(

    private val mealDao: MealDao

) : MealRepository {

    override suspend fun add(meal: Meal, planId: Long): Long {
        return mealDao.add(meal.toEntity(planId))
    }

    override suspend fun findById(id: Long): Meal? {
        return mealDao.findById(id)?.toDomain()
    }

    override suspend fun findByPlanId(planId: Long): List<Meal> {
        return mealDao.findByPlanId(planId).map(MealEntity::toDomain)
    }

    override suspend fun findMealWithOptions(id: Long): Meal? {
        return mealDao.findMealWithOptions(id)?.toDomain()
    }

    override suspend fun update(meal: Meal, planId: Long): Int {
        return mealDao.update(meal.toEntity(planId))
    }

    override suspend fun delete(meal: Meal, planId: Long): Int {
        return mealDao.delete(meal.toEntity(planId))
    }

    override suspend fun deleteById(id: Long): Int {
        return mealDao.deleteById(id)
    }

}
