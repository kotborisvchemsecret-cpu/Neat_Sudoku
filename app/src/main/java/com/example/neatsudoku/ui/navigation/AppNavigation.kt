package com.example.neatsudoku.ui.navigation

import androidx.compose.runtime.Composable
import com.example.neatsudoku.ui.screens.mainmenu.MainMenuScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.neatsudoku.ui.screens.game.GameScreen
import com.example.neatsudoku.ui.screens.settings.SettingsScreen
import com.example.neatsudoku.ui.screens.game.WinScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main_menu"
    ){
        composable("main_menu"){
            MainMenuScreen(
                onNewGameClick = {
                    navController.navigate(Screen.Game.route)
                },
                onContinueClick = {
                    navController.navigate(Screen.Game.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        composable(Screen.Game.route) {
            GameScreen(onWin = {navController.navigate(Screen.Win.route){ launchSingleTop = true} })
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
        composable(Screen.Win.route) {
            WinScreen(
                onPlayAgainClick = {
                    navController.navigate(Screen.Game.route) {
                        popUpTo(Screen.MainMenu.route)
                        launchSingleTop = true
                    }
                },
                onMainMenuClick = {
                    navController.popBackStack(
                        Screen.MainMenu.route,
                        inclusive = false
                    )
                }
            )
        }
    }
}