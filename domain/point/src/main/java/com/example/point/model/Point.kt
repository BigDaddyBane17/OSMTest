package com.example.point.model

data class Point(
    val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val name: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)