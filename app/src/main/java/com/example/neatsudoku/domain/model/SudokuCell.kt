package com.example.neatsudoku.domain.model

data class SudokuCell (
    val value: Int?,
    val isGiven: Boolean,
    val row: Int,
    val column: Int,
    val pencilMarks: Set<Int> = emptySet()
)
