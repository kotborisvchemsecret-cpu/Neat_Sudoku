package com.example.neatsudoku.ui.viewmodel

import com.example.neatsudoku.domain.model.SudokuGame

data class GameUiState (
    val game: SudokuGame? = null,
    val selectedRow: Int? = null,
    val selectedColumn: Int? = null,
    val incorrectCells: Set<Pair<Int, Int>> = emptySet()
)