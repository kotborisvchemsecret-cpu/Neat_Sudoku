package com.example.neatsudoku.ui.viewmodel

import com.example.neatsudoku.domain.model.Difficulty
import com.example.neatsudoku.domain.model.SudokuGame

data class GameUiState(
    val game: SudokuGame? = null,
    val difficulty: Difficulty = Difficulty.EASY,
    val isWon: Boolean = false,
    val isGameOver: Boolean = false,
    val mistakes: Int = 0,
    val selectedRow: Int? = null,
    val selectedColumn: Int? = null,
    val incorrectCells: Set<Pair<Int, Int>> = emptySet(),
    val isPencilMode: Boolean = false
)