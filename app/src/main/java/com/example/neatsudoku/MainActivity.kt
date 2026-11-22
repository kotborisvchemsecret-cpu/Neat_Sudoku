package com.example.neatsudoku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.neatsudoku.ui.navigation.AppNavigation
import com.example.neatsudoku.ui.theme.NeatSudokuTheme
import com.example.neatsudoku.ui.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val settings by settingsViewModel.uiState.collectAsState()

            NeatSudokuTheme(themeMode = settings.themeMode) {
                AppNavigation(settingsViewModel = settingsViewModel)
            }
        }

    }
}

