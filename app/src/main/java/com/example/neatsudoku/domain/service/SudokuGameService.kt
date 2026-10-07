package com.example.neatsudoku.domain.service

import com.example.neatsudoku.domain.model.Difficulty
import com.example.neatsudoku.domain.model.SudokuBoard
import com.example.neatsudoku.domain.model.SudokuCell
import com.example.neatsudoku.domain.model.SudokuGame

class SudokuGameService : ISudokuGameService {
    private val generator = SudokuGenerator()

    override fun createNewGame(
        difficulty: Difficulty
    ): SudokuGame {
        val solution = generator.generateSolution()
        val puzzle = generator.generatePuzzle(solution,difficulty)
        return SudokuGame(
            puzzle = puzzle,
            board = puzzle.copy(
                cells = puzzle.cells.map { row ->
                    row.map { cell ->
                        cell.copy()
                    }
                }
            ),
            solution = solution
        )
    }
}