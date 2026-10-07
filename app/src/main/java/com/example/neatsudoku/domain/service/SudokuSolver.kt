package com.example.neatsudoku.domain.service

import com.example.neatsudoku.domain.model.SudokuBoard
import com.example.neatsudoku.domain.service.constraints.ISudokuConstraint

class SudokuSolver(private val constraint: ISudokuConstraint) {
    companion object{
        private const val SIZE = 9
        private const val MAX_SOLUTIONS = 2
    }

    fun countSolutions(board: SudokuBoard): Int {
        val grid = board.cells.map { row ->
            row.map { cell ->
                cell.value ?: 0
            }.toIntArray()
        }.toTypedArray()

        return countSolutions(grid)
    }

    private fun countSolutions(
        grid: Array<IntArray>
    ): Int {
        val emptyCell = findEmptyCell(grid) ?: return 1

        val row = emptyCell.first
        val column = emptyCell.second

        var solutionCount = 0

        for (number in 1..SIZE) {
            if (!constraint.isValid(grid, row, column, number)) {
                continue
            }

            grid[row][column] = number

            solutionCount += countSolutions(grid)

            grid[row][column] = 0

            if (solutionCount >= MAX_SOLUTIONS) {
                return MAX_SOLUTIONS
            }
        }

        return solutionCount
    }
    private fun findEmptyCell(
        grid: Array<IntArray>
    ): Pair<Int, Int>? {
        for (row in 0 until SIZE) {
            for (column in 0 until SIZE) {
                if (grid[row][column] == 0) {
                    return Pair(row, column)
                }
            }
        }

        return null
    }
}