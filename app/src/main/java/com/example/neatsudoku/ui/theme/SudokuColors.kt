package com.example.neatsudoku.ui.theme

import androidx.compose.ui.graphics.Color

data class SudokuColors (
    val selectedCell: Color,
    val selectedRowColumn: Color,
    val gridLine: Color,
    val boxLine: Color,
    val givenNumber: Color,
    val playerNumber: Color,
    val incorrectNumber: Color
)