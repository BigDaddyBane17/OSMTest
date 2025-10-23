package com.example.point

import com.example.point.model.Point
import com.example.point.repository.PointRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PointRepositoryImpl @Inject constructor(
    private val pointDao: PointDao
) : PointRepository {
    override fun getAllPoints(): Flow<List<Point>> =
        pointDao.getAllPoints().map { entities -> entities.map { it.toDomain() } }
    
    override suspend fun getPointById(id: Long): Point? =
        pointDao.getPointById(id)?.toDomain()
    
    override suspend fun insertPoint(point: Point): Long =
        pointDao.insertPoint(point.toEntity())
    
    override suspend fun updatePoint(point: Point) =
        pointDao.updatePoint(point.toEntity())
    
    override suspend fun deletePoint(point: Point) =
        pointDao.deletePoint(point.toEntity())
    
    override suspend fun deletePointById(id: Long) = 
        pointDao.deletePointById(id)
}
