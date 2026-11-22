package com.example.neatsudoku.data.repository

import android.content.Context
import androidx.core.content.edit
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

class SettingsRepository(context: Context) {

    private val preferences = context.applicationContext.getSharedPreferences(
        "sudoku_settings",
        Context.MODE_PRIVATE
    )

    fun loadPlayerColor() = Color(
        preferences.getInt("player_color", Color(0xFF3F51B5).toArgb())
    )

    fun loadIncorrectColor() = Color(
        preferences.getInt("incorrect_color", Color.Red.toArgb())
    )

    fun loadAutoRemovePencilNotes() =
        preferences.getBoolean("auto_remove_pencil_notes", false)

    fun loadHideCompletedNumbers() =
        preferences.getBoolean("hide_completed_numbers", false)

    fun loadThemeMode() =
        preferences.getString("theme_mode", "SYSTEM") ?: "SYSTEM"

    fun savePlayerColor(color: Color) {
        preferences.edit { putInt("player_color", color.toArgb()) }
    }

    fun saveIncorrectColor(color: Color) {
        preferences.edit { putInt("incorrect_color", color.toArgb()) }
    }

    fun saveAutoRemovePencilNotes(enabled: Boolean) {
        preferences.edit { putBoolean("auto_remove_pencil_notes", enabled) }
    }

    fun saveHideCompletedNumbers(enabled: Boolean) {
        preferences.edit { putBoolean("hide_completed_numbers", enabled) }
    }

    fun saveThemeMode(mode: String) {
        preferences.edit { putString("theme_mode", mode) }
    }
}