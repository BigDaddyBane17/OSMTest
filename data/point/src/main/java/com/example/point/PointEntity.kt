package com.example.point

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.point.model.Point

@Entity(tableName = "points")
data class PointEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val name: String? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): Point = Point(
        id = id,
        latitude = latitude,
        longitude = longitude,
        name = name,
        createdAt = createdAt
    )
}

fun Point.toEntity(): PointEntity = PointEntity(
    id = id,
    latitude = latitude,
    longitude = longitude,
    name = name,
    createdAt = createdAt
)
