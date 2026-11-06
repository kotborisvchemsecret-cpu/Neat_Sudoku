package com.example.neatsudoku.domain.model

data class SudokuCell (
    val value: Int?,
    // whether the number was generated on game creation
    val isGiven: Boolean,
    val row: Int,
    val column: Int,
    // noncommittal 'guesses' for potential numbers in a given cell
    val pencilMarks: Set<Int> = emptySet()
)
