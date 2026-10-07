package com.example.neatsudoku.domain.model

import androidx.compose.material3.FabPosition

data class SudokuCell (
    val value: Int?,
    val isGiven: Boolean,
    val row: Int,
    val column: Int
)
