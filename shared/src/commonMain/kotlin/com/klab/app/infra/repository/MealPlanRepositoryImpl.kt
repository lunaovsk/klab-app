package com.klab.app.infra.repository

import com.klab.app.domain.model.meal.MealPlan
import com.klab.app.domain.repository.MealPlanRepository
import com.klab.app.infra.dao.MealPlanDao
import com.klab.app.infra.local.MealPlanEntity
import com.klab.app.infra.mapper.toDomain
import com.klab.app.infra.mapper.toEntity

class MealPlanRepositoryImpl (

    private val mealPlanDao: MealPlanDao

): MealPlanRepository {

    override suspend fun insert(mealPlan: MealPlan): Long {
        return mealPlanDao.insert(mealPlan.toEntity())
    }

    override suspend fun findById(id: Long): MealPlan? {
        return mealPlanDao.findById(id)?.toDomain()
    }

    override suspend fun findAll(): List<MealPlan> {
        return mealPlanDao.findAll().map(MealPlanEntity::toDomain)
    }

    override suspend fun findMealPlanById(id: Long): MealPlan? {
        return mealPlanDao.findMealPlanById(id)?.toDomain()
    }

    override suspend fun update(mealPlan: MealPlan): Int {
        return mealPlanDao.update(mealPlan.toEntity())
    }

    override suspend fun delete(mealPlan: MealPlan): Int {
        return mealPlanDao.delete(mealPlan.toEntity())
    }

    override suspend fun deleteById(id: Long): Int {
        return mealPlanDao.deleteById(id)
    }

}
