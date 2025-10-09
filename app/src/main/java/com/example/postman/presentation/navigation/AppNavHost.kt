package com.example.postman.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.postman.presentation.collection.CollectionScreen
import com.example.postman.presentation.collection.CollectionViewModel
import com.example.postman.presentation.history.HistoryScreen
import com.example.postman.presentation.history.HistoryViewModel
import com.example.postman.presentation.home.HomeScreen
import com.example.postman.presentation.home.HomeViewModel

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel = hiltViewModel(),
    historyViewModel: HistoryViewModel = hiltViewModel(),
    collectionViewModel: CollectionViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreen.route
    ) {
        composable(
            Screens.HomeScreen.route,
            arguments = listOf(navArgument(Screens.ARG_REQUEST_ID) {
                type = NavType.IntType
                defaultValue = -1
            },navArgument(Screens.ARG_SOURCE) {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { backStackEntry ->
            var requestId = backStackEntry.arguments?.getInt(Screens.ARG_REQUEST_ID) ?: -1
            var source = backStackEntry.arguments?.getString(Screens.ARG_SOURCE) ?: ""
            HomeScreen(
                homeViewModel,
                requestId,
                source,
                onNavigateToHistory = {
                    requestId = -1
                    navController.navigate(Screens.HistoryScreen.route)
                },
                onNavigateToCollection = {
                    navController.navigate(Screens.CollectionScreen.route)
                }
            )
        }

        composable(Screens.HistoryScreen.route) {
            HistoryScreen(
                navController,
                historyViewModel,
                onHistoryItemClick = { requestId ->
                    navController.navigate(
                        Screens.HomeScreen.createRoute(
                            requestId,
                            Screens.ROUTE_HISTORY_SCREEN
                        )
                    )
                }
            )
        }
        composable(Screens.CollectionScreen.route) {
            CollectionScreen(
                navController,
                collectionViewModel,
                onCollectionItemClick = { requestId ->
                    navController.navigate(
                        Screens.HomeScreen.createRoute(
                            requestId,
                            Screens.ROUTE_COLLECTION_SCREEN
                        )
                    )
                })
        }
    }
}