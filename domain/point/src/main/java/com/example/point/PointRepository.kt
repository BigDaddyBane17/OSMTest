package com.example.point

import kotlinx.coroutines.flow.Flow

interface PointRepository {
    fun getAllPoints(): Flow<List<Point>>
    suspend fun getPointById(id: Long): Point?
    suspend fun insertPoint(point: Point): Long
    suspend fun updatePoint(point: Point)
    suspend fun deletePoint(point: Point)
    suspend fun deletePointById(id: Long)
}
