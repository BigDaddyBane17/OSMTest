package com.example.point.usecase

import com.example.point.model.Point
import com.example.point.repository.PointRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class PointUseCases @Inject constructor(
    val getAllPoints: GetAllPoints,
    val getPointById: GetPointById,
    val insertPoint: InsertPoint,
    val updatePoint: UpdatePoint,
    val deletePoint: DeletePoint
)

class GetAllPoints @Inject constructor(
    private val repository: PointRepository
) {
    operator fun invoke(): Flow<List<Point>> = repository.getAllPoints()
}

class GetPointById @Inject constructor(
    private val repository: PointRepository
) {
    suspend operator fun invoke(id: Long): Point? = repository.getPointById(id)
}

class InsertPoint @Inject constructor(
    private val repository: PointRepository
) {
    suspend operator fun invoke(point: Point): Long = repository.insertPoint(point)
}

class UpdatePoint @Inject constructor(
    private val repository: PointRepository
) {
    suspend operator fun invoke(point: Point) = repository.updatePoint(point)
}

class DeletePoint @Inject constructor(
    private val repository: PointRepository
) {
    suspend operator fun invoke(point: Point) = repository.deletePoint(point)
}
