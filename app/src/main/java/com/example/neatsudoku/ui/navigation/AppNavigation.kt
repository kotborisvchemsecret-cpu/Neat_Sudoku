package com.example.neatsudoku.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.neatsudoku.ui.viewmodel.SettingsViewModel

@Composable
fun AppNavigation(
    settingsViewModel: SettingsViewModel
) {
    val settings by settingsViewModel.uiState.collectAsState()
    val navController = rememberNavController()

    val gameViewModel: GameViewModel = viewModel()
    val gameState by gameViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.MainMenu.route
    ) {
        composable(Screen.MainMenu.route) {
            MainMenuScreen(
                canContinue = gameState.game != null &&
                        !gameState.isWon &&
                        !gameState.isGameOver,
                onNewGameClick = {
                    navController.navigate(Screen.DifficultySelection.route)
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
                playerNumberColor = settings.playerNumberColor,
                incorrectNumberColor = settings.incorrectNumberColor,
                autoRemovePencilNotes = settings.autoRemovePencilNotes,
                hideCompletedNumbers = settings.hideCompletedNumbers,
                viewModel = gameViewModel
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                playerNumberColor = settings.playerNumberColor,
                incorrectNumberColor = settings.incorrectNumberColor,
                autoRemovePencilNotes = settings.autoRemovePencilNotes,
                hideCompletedNumbers = settings.hideCompletedNumbers,
                themeMode = settings.themeMode,

                onPlayerColorChange = settingsViewModel::setPlayerNumberColor,
                onIncorrectColorChange = settingsViewModel::setIncorrectNumberColor,
                onAutoRemovePencilNotesChange = settingsViewModel::setAutoRemovePencilNotes,
                onHideCompletedNumbersChange = settingsViewModel::setHideCompletedNumbers,
                onThemeModeChange = settingsViewModel::setThemeMode
            )
        }

        composable(Screen.Win.route) {
            WinScreen(
                onPlayAgainClick = {
                    navController.navigate(Screen.DifficultySelection.route) {
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

        composable(Screen.GameOver.route) {
            GameOverScreen(
                onTryAgainClick = {
                    navController.navigate(Screen.DifficultySelection.route) {
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