package com.example.postman.presentation.navigation

sealed class Screens(val route: String) {
    object HomeScreen : Screens("Home?historyId={historyId}"){ //historyId is optional
        fun createRoute(historyId: Int) = "Home?historyId=$historyId"
    }
    object HistoryScreen : Screens("History")
    object CollectionScreen: Screens("Collections")
}