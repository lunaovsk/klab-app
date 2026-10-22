package com.klab.app.infra.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.klab.app.domain.model.food.Food
import com.klab.app.domain.repository.FoodRepository
import com.klab.app.infra.dao.FoodDao
import com.klab.app.infra.local.FoodEntity
import com.klab.app.infra.mapper.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FoodRepositoryImpl(

    private val foodDao: FoodDao

) : FoodRepository {

    override suspend fun findById(id: Long): Food? {
        return foodDao.findById(id)?.toDomain()
    }

    override suspend fun listAllGroups(): List<String> {
        return foodDao.listAllGroups()
    }

    override fun searchByDescription(query: String): Flow<PagingData<Food>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { foodDao.searchByDescription(query) }
        ).flow.map { pagingData ->
            pagingData.map(FoodEntity::toDomain)
        }
    }

    override fun findByGroup(group: String): Flow<PagingData<Food>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { foodDao.findByGroup(group) }
        ).flow.map { pagingData ->
            pagingData.map(FoodEntity::toDomain)
        }
    }

}
