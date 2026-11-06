package com.example.neatsudoku.domain.service.constraints


//Standard rules of sudoku: numbers cant repeat in row, column and box
class StandardSudokuConstraint(override val size: Int = 9, private val boxSize: Int = 3) : ISudokuConstraint {

    override fun isValid(
        grid: Array<IntArray>,
        row: Int,
        column: Int,
        number: Int
    ): Boolean {
        return isValidRow(grid, row, number) &&
                isValidColumn(grid, column, number) &&
                isValidBox(grid, row, column, number)
    }

    private fun isValidRow(
        grid: Array<IntArray>,
        row: Int,
        number: Int
    ): Boolean {
        for (column in 0 until size) {
            if (grid[row][column] == number) {
                return false
            }
        }

        return true
    }

    private fun isValidColumn(
        grid: Array<IntArray>,
        column: Int,
        number: Int
    ): Boolean {
        for (row in 0 until size) {
            if (grid[row][column] == number) {
                return false
            }
        }

        return true
    }

    private fun isValidBox(
        grid: Array<IntArray>,
        row: Int,
        column: Int,
        number: Int
    ): Boolean {
        val boxRow = row - row % boxSize
        val boxColumn = column - column % boxSize

        for (currentRow in boxRow until boxRow + boxSize) {
            for (currentColumn in boxColumn until boxColumn + boxSize) {
                if (grid[currentRow][currentColumn] == number) {
                    return false
                }
            }
        }

        return true
    }
}