package com.example.neatsudoku.domain.service

import com.example.neatsudoku.domain.model.Difficulty
import com.example.neatsudoku.domain.model.SudokuGame

interface ISudokuGameService {
    /**
     * creates a new game of sudoku of given difficulty
     * @param difficulty the difficulty chosen for sudoku creation
     * @return the created game
     */
    fun createNewGame(difficulty: Difficulty): SudokuGame
}