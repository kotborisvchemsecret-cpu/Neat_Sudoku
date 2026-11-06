package com.example.neatsudoku.domain.service.constraints

interface ISudokuConstraint {
    val size: Int

    /**
     * checks whether a placement of a number in a sudoku cell is valid
     * @param grid: sudoku grid
     * @param row: the row placement of a number in the grid
     * @param column: the column placement of a number in the grid
     * @param number: the number whose placement is being validated
     * @return whether the placement is valid
     */
    fun isValid(grid: Array<IntArray>, row: Int, column: Int, number: Int): Boolean
}