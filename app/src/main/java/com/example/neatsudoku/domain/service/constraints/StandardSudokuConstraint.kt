package com.example.neatsudoku.domain.service.constraints

class StandardSudokuConstraint : ISudokuConstraint {
    companion object {
        private const val SIZE = 9
        private const val BOX_SIZE = 3
    }

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
        for (column in 0 until SIZE) {
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
        for (row in 0 until SIZE) {
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
        val boxRow = row - row % BOX_SIZE
        val boxColumn = column - column % BOX_SIZE

        for (currentRow in boxRow until boxRow + BOX_SIZE) {
            for (currentColumn in boxColumn until boxColumn + BOX_SIZE) {
                if (grid[currentRow][currentColumn] == number) {
                    return false
                }
            }
        }

        return true
    }
}