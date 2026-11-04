package com.example.neatsudoku.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.neatsudoku.domain.model.Difficulty
import com.example.neatsudoku.ui.screens.difficulty.DifficultySelectionScreen
import com.example.neatsudoku.ui.screens.game.GameOverScreen
import com.example.neatsudoku.ui.screens.game.GameScreen
import com.example.neatsudoku.ui.screens.game.WinScreen
import com.example.neatsudoku.ui.screens.mainmenu.MainMenuScreen
import com.example.neatsudoku.ui.screens.settings.SettingsScreen
import com.example.neatsudoku.ui.viewmodel.GameViewModel
import androidx.core.content.edit

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Shared game ViewModel
    val gameViewModel: GameViewModel = viewModel()
    val gameState by gameViewModel.uiState.collectAsState()

    // Saved color preferences
    val context = LocalContext.current

    val preferences = remember {
        context.getSharedPreferences(
            "sudoku_settings",
            Context.MODE_PRIVATE
        )
    }

    var playerNumberColor by remember {
        mutableStateOf(
            Color(
                preferences.getInt(
                    "player_color",
                    Color(0xFF3F51B5).toArgb()
                )
            )
        )
    }

    var incorrectNumberColor by remember {
        mutableStateOf(
            Color(
                preferences.getInt(
                    "incorrect_color",
                    Color.Red.toArgb()
                )
            )
        )
    }

    NavHost(
        navController = navController,
        startDestination = Screen.MainMenu.route
    ) {

        // MAIN MENU
        composable(Screen.MainMenu.route) {
            MainMenuScreen(
                canContinue = gameState.game != null &&
                        !gameState.isWon &&
                        !gameState.isGameOver,

                onNewGameClick = {
                    navController.navigate(
                        Screen.DifficultySelection.route
                    )
                },

                onContinueClick = {
                    gameViewModel.ensureGameExists()

                    navController.navigate(Screen.Game.route) {
                        launchSingleTop = true
                    }
                },

                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        // DIFFICULTY SELECTION
        composable(Screen.DifficultySelection.route) {
            DifficultySelectionScreen(
                onStartGame = { difficulty: Difficulty ->

                    gameViewModel.startNewGame(difficulty)

                    navController.navigate(Screen.Game.route) {
                        popUpTo(Screen.DifficultySelection.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },

                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // GAME
        composable(Screen.Game.route) {
            GameScreen(
                onWin = {
                    navController.navigate(Screen.Win.route) {
                        launchSingleTop = true
                    }
                },

                onGameOver = {
                    navController.navigate(Screen.GameOver.route) {
                        launchSingleTop = true
                    }
                },

                playerNumberColor = playerNumberColor,
                incorrectNumberColor = incorrectNumberColor,
                viewModel = gameViewModel
            )
        }

        // SETTINGS
        composable(Screen.Settings.route) {
            SettingsScreen(
                playerNumberColor = playerNumberColor,
                incorrectNumberColor = incorrectNumberColor,

                onPlayerColorChange = { color ->
                    playerNumberColor = color

                    preferences.edit()
                        .putInt("player_color", color.toArgb())
                        .apply()
                },

                onIncorrectColorChange = { color ->
                    incorrectNumberColor = color

                    preferences.edit {
                        putInt("incorrect_color", color.toArgb())
                    }
                }
            )
        }

        // WIN
        composable(Screen.Win.route) {
            WinScreen(
                onPlayAgainClick = {
                    navController.navigate(
                        Screen.DifficultySelection.route
                    ) {
                        popUpTo(Screen.MainMenu.route)
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

        // GAME OVER
        composable(Screen.GameOver.route) {
            GameOverScreen(
                onTryAgainClick = {
                    navController.navigate(
                        Screen.DifficultySelection.route
                    ) {
                        popUpTo(Screen.MainMenu.route)
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