package com.klab.app.domain.repository

import androidx.paging.PagingData
import com.klab.app.domain.model.food.Food
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    suspend fun findById(id: Long): Food?
    suspend fun listAllGroups(): List<String>
    fun searchByDescription(query: String): Flow<PagingData<Food>>
    fun findByGroup(group: String): Flow<PagingData<Food>>

}
