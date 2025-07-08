package com.example.postman.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.postman.data.local.appDatabase.RoomDatabase
import com.example.postman.data.remote.ApiClient
import com.example.postman.data.repository.ApiRepositoryImp
import com.example.postman.data.repository.HistoryRequestRepositoryImp
import com.example.postman.presentation.Screens
import com.example.postman.presentation.history.HistoryScreen
import com.example.postman.presentation.history.HistoryViewModel
import com.example.postman.presentation.home.HomeScreen
import com.example.postman.presentation.home.HomeViewModel

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    val db = Room.databaseBuilder(
        LocalContext.current,
        RoomDatabase::class.java, Screens.HistoryScreen.route
    ).build()
    val dao = db.historyRequestDao()
    val historyRepo = HistoryRequestRepositoryImp(dao)
    val homeViewModel: HomeViewModel=viewModel(
        factory = object : ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(ApiRepositoryImp(ApiClient.createApiService()),historyRepo) as T
            }
        }
    )
    val historyViewModel: HistoryViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
           // @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HistoryViewModel(historyRepo) as T
            }
        }
    )

    NavHost(
        navController = navController,
        startDestination = Screens.HomeScreen.route
    ) {
        composable(Screens.HomeScreen.route) {
            HomeScreen(
                modifier = Modifier.padding(4.dp),
                homeViewModel,
                historyViewModel,
                onNavigateToHistory = {
                    navController.navigate(Screens.HistoryScreen.route)
                }
            )
        }
        composable(Screens.HistoryScreen.route) {
            HistoryScreen(navController,historyViewModel)
        }
    }

}