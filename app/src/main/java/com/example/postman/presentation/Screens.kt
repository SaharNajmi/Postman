package com.example.postman.presentation

sealed class Screens(val route: String) {
    object HomeScreen : Screens("Home")
    object HistoryScreen : Screens("History")
}