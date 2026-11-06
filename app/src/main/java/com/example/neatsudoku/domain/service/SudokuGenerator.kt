package com.example.neatsudoku.domain.service

import com.example.neatsudoku.domain.model.Difficulty
import com.example.neatsudoku.domain.model.SudokuBoard
import com.example.neatsudoku.domain.model.SudokuCell
import com.example.neatsudoku.domain.service.constraints.ISudokuConstraint
import com.example.neatsudoku.domain.service.constraints.StandardSudokuConstraint

/**
 * Generates valid Sudoku solutions and puzzles under given constraints
 *
 * solver ensures puzzles have a unique solution
 */
class SudokuGenerator( private val constraint: ISudokuConstraint = StandardSudokuConstraint(),
                        private val solver: SudokuSolver = SudokuSolver(constraint)
) {

    fun generatePuzzle(solution: SudokuBoard, difficulty: Difficulty): SudokuBoard {
        val grid = solution.cells.map { row ->
            row.map { cell ->
                cell.value ?: 0
            }.toIntArray()
        }.toTypedArray()

        val targetClues =
            (difficulty.minimumClues..difficulty.maximumClues).random()

        val positions = (0 until constraint.size * constraint.size).shuffled()

        var clues = constraint.size * constraint.size

        for (position in positions) {
            if (clues <= targetClues) {
                break
            }

            val row = position / constraint.size
            val column = position % constraint.size

            val originalValue = grid[row][column]

            grid[row][column] = 0

            val puzzle = createPuzzleBoard(grid)

            if (solver.countSolutions(puzzle) == 1) {
                clues--
            } else {
                grid[row][column] = originalValue
            }
        }

        return createPuzzleBoard(grid)
    }

    fun generateSolution(): SudokuBoard{
        val grid = Array(constraint.size){ IntArray(constraint.size) }
        fillGrid(grid, 0, 0)
        return createBoard(grid)
    }

    private fun fillGrid(grid: Array<IntArray>, row: Int, column: Int): Boolean {
        if (row == constraint.size) {
            return true
        }

        val numbers = (1..constraint.size).shuffled()

        for (number in numbers) {
            if (!constraint.isValid(grid, row, column, number)) {
                continue
            }

            grid[row][column] = number

            val nextColumn = (column + 1) % constraint.size
            val nextRow = row + if (nextColumn == 0) 1 else 0

            if (fillGrid(grid, nextRow, nextColumn)) {
                return true
            }

            grid[row][column] = 0
        }

        return false
    }

    private fun createBoard(grid: Array<IntArray>): SudokuBoard {
        val cells = List(constraint.size) { row ->
            List(constraint.size) { column ->
                SudokuCell(
                    row = row,
                    column = column,
                    value = grid[row][column],
                    isGiven = true
                )
            }
        }

        return SudokuBoard(cells)
    }

    private fun createPuzzleBoard(
        grid: Array<IntArray>
    ): SudokuBoard {
        val cells = List(constraint.size) { row ->
            List(constraint.size) { column ->
                val value = grid[row][column]

                SudokuCell(
                    row = row,
                    column = column,
                    value = if (value == 0) null else value,
                    isGiven = value != 0
                )
            }
        }

        return SudokuBoard(cells)
    }
}