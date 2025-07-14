package com.example.postman.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.postman.presentation.navigation.Screens
import com.example.postman.presentation.history.HistoryScreen
import com.example.postman.presentation.history.HistoryViewModel
import com.example.postman.presentation.home.HomeScreen
import com.example.postman.presentation.home.HomeViewModel

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    historyViewModel: HistoryViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreen.route
    ) {
        composable(
            Screens.HomeScreen.route,
            arguments = listOf(navArgument("historyId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            var historyId = backStackEntry.arguments?.getInt("historyId") ?: -1
            HomeScreen(
                homeViewModel,
                historyId,
                onNavigateToHistory = {
                    historyId = -1
                    navController.navigate(Screens.HistoryScreen.route)
                }
            )
        }

        composable(Screens.HistoryScreen.route) {
            HistoryScreen(
                navController,
                historyViewModel,
                onHistoryItemClick = { historyId ->
                    navController.navigate(Screens.HomeScreen.createRoute(historyId))
                }
            )
        }
    }
}