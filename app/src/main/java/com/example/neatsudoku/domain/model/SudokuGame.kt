package com.example.neatsudoku.domain.model

data class SudokuGame (
    val puzzle: SudokuBoard,
    val board: SudokuBoard,
    val solution: SudokuBoard
)