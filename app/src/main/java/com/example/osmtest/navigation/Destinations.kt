package com.example.osmtest.navigation

sealed class Destinations(val route: String) {
    data object Map : Destinations("map")
    data object MapDetails : Destinations("mapDetails/{pointId}") {
        fun route(pointId: Long) = "mapDetails/$pointId"
        const val argPointId = "pointId"
    }
}