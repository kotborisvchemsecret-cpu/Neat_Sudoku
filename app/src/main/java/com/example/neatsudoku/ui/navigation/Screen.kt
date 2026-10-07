package com.example.neatsudoku.ui.navigation

import androidx.compose.ui.text.font.FontVariation

sealed class Screen(val route: String) {
    data object  MainMenu : Screen("main_menu")
    data object Game : Screen("game")
    data object Settings : Screen("settings")
}