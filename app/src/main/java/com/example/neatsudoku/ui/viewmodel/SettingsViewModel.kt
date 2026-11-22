package com.example.neatsudoku.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.compose.ui.graphics.Color
import com.example.neatsudoku.data.repository.SettingsRepository
import com.example.neatsudoku.ui.theme.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SettingsUiState(
    val playerNumberColor: Color = Color(0xFF3F51B5),
    val incorrectNumberColor: Color = Color.Red,
    val autoRemovePencilNotes: Boolean = false,
    val hideCompletedNumbers: Boolean = false,
    val themeMode: ThemeMode = ThemeMode.SYSTEM
)

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingsRepository(application)

    private val _uiState = MutableStateFlow(
        SettingsUiState(
            playerNumberColor = repository.loadPlayerColor(),
            incorrectNumberColor = repository.loadIncorrectColor(),
            autoRemovePencilNotes = repository.loadAutoRemovePencilNotes(),
            hideCompletedNumbers = repository.loadHideCompletedNumbers(),
            themeMode = runCatching {
                ThemeMode.valueOf(repository.loadThemeMode())
            }.getOrDefault(ThemeMode.SYSTEM)
        )
    )

    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun setPlayerNumberColor(color: Color) {
        repository.savePlayerColor(color)
        _uiState.value = _uiState.value.copy(playerNumberColor = color)
    }

    fun setIncorrectNumberColor(color: Color) {
        repository.saveIncorrectColor(color)
        _uiState.value = _uiState.value.copy(incorrectNumberColor = color)
    }

    fun setAutoRemovePencilNotes(enabled: Boolean) {
        repository.saveAutoRemovePencilNotes(enabled)
        _uiState.value = _uiState.value.copy(autoRemovePencilNotes = enabled)
    }

    fun setHideCompletedNumbers(enabled: Boolean) {
        repository.saveHideCompletedNumbers(enabled)
        _uiState.value = _uiState.value.copy(hideCompletedNumbers = enabled)
    }

    fun setThemeMode(mode: ThemeMode) {
        repository.saveThemeMode(mode.name)
        _uiState.value = _uiState.value.copy(themeMode = mode)
    }
}