package com.example.neatsudoku.domain.service

import com.example.neatsudoku.domain.model.Difficulty
import com.example.neatsudoku.domain.model.SudokuGame

interface ISudokuGameService {
    fun createNewGame(difficulty: Difficulty): SudokuGame
}