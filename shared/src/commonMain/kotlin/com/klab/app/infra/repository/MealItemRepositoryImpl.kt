package com.klab.app.infra.repository

import com.klab.app.domain.model.meal.MealItem
import com.klab.app.domain.repository.MealItemRepository
import com.klab.app.infra.dao.MealItemDao
import com.klab.app.infra.local.MealItemEntity
import com.klab.app.infra.mapper.toDomain
import com.klab.app.infra.mapper.toEntity

class MealItemRepositoryImpl(

    private val mealItemDao: MealItemDao

) : MealItemRepository {

    override suspend fun insert(mealItem: MealItem, optionId: Long): Long {
        return mealItemDao.insert(mealItem.toEntity(optionId))
    }

    override suspend fun findById(id: Long): MealItem? {
        return mealItemDao.findById(id)?.toDomain()
    }

    override suspend fun findByOptionId(optionId: Long): List<MealItem> {
        return mealItemDao.findByOptionId(optionId).map(MealItemEntity::toDomain)
    }

    override suspend fun findItemWithFood(id: Long): MealItem? {
        return mealItemDao.findItemWithFood(id)?.toDomain()
    }

    override suspend fun update(mealItem: MealItem, optionId: Long): Int {
        return mealItemDao.update(mealItem.toEntity(optionId))
    }

    override suspend fun delete(mealItem: MealItem, optionId: Long): Int {
        return mealItemDao.delete(mealItem.toEntity(optionId))
    }

    override suspend fun deleteById(id: Long): Int {
        return mealItemDao.deleteById(id)
    }

}
