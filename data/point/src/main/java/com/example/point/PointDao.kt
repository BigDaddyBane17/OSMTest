package com.example.point

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PointDao {
    @Query("SELECT * FROM points ORDER BY createdAt DESC")
    fun getAllPoints(): Flow<List<PointEntity>>
    
    @Query("SELECT * FROM points WHERE id = :id")
    suspend fun getPointById(id: Long): PointEntity?
    
    @Insert
    suspend fun insertPoint(point: PointEntity): Long
    
    @Update
    suspend fun updatePoint(point: PointEntity)
    
    @Delete
    suspend fun deletePoint(point: PointEntity)
    
    @Query("DELETE FROM points WHERE id = :id")
    suspend fun deletePointById(id: Long)
}
