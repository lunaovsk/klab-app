package com.klab.app.infra.repository

import com.klab.app.domain.model.meal.MealOption
import com.klab.app.domain.repository.MealOptionRepository
import com.klab.app.infra.dao.MealOptionDao
import com.klab.app.infra.local.MealOptionEntity
import com.klab.app.infra.mapper.toDomain
import com.klab.app.infra.mapper.toEntity

class MealOptionRepositoryImpl(

    private val mealOptionDao: MealOptionDao

) : MealOptionRepository {

    override suspend fun insert(mealOption: MealOption, mealId: Long): Long {
        return mealOptionDao.insert(mealOption.toEntity(mealId))
    }

    override suspend fun findById(id: Long): MealOption? {
        return mealOptionDao.findById(id)?.toDomain()
    }

    override suspend fun findByMealId(mealId: Long): List<MealOption> {
        return mealOptionDao.findByMealId(mealId).map(MealOptionEntity::toDomain)
    }

    override suspend fun findOptionWithItems(id: Long): MealOption? {
        return mealOptionDao.findOptionWithItems(id)?.toDomain()
    }

    override suspend fun update(mealOption: MealOption, mealId: Long): Int {
        return mealOptionDao.update(mealOption.toEntity(mealId))
    }

    override suspend fun delete(mealOption: MealOption, mealId: Long): Int {
        return mealOptionDao.delete(mealOption.toEntity(mealId))
    }

    override suspend fun deleteById(id: Long): Int {
        return mealOptionDao.deleteById(id)
    }

}
