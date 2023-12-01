package com.example.aprice.navigation

sealed class Screen(val route: String) {
    object HomeScreen: Screen("home_screen")
    object DetailScreen: Screen("detail_screen")
    object SettingsScreen: Screen("settings_screen")
    object HistoryScreen: Screen("history_screen")
}