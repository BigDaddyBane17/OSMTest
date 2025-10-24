package com.example.osmtest.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.map.MapScreen
import com.example.mapdetails.MapDetailsScreen

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Destinations.Map.route) {
        composable(route = Destinations.Map.route) {
            MapScreen(
                onNavigateToDetails = { id ->
                    navController.navigate(Destinations.MapDetails.route(id)) {
                        launchSingleTop = true
                    }
                }
            )
        }


        composable(
            route = Destinations.MapDetails.route,
            arguments = listOf(
                navArgument(Destinations.MapDetails.argPointId) { type = NavType.LongType }
            )
        ) {
            MapDetailsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}