package com.klab.app.infra.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.klab.app.infra.local.FoodEntity

@Dao
interface FoodDao {

    @Query("SELECT * FROM tb_tbca WHERE id = :id")
    suspend fun findById(id: Long): PagingSource<Long, FoodEntity>

    @Query("SELECT * FROM tb_tbca WHERE description LIKE '%' || :query || '%' ORDER BY description ASC LIMIT 50")
    suspend fun searchByDescription(query: String): PagingSource<Long, FoodEntity>

    @Query("SELECT DISTINCT `group` FROM tb_tbca ORDER BY `group` ASC")
    suspend fun listAllGroups(): PagingSource<Long, FoodEntity>

    @Query("SELECT * FROM tb_tbca WHERE `group` = :group ORDER BY description ASC")
    suspend fun findByGroup(group: String): PagingSource<Long, FoodEntity>
}
