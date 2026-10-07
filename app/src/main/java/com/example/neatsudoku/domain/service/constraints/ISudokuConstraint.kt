package com.example.neatsudoku.domain.service.constraints

interface ISudokuConstraint {

    fun isValid(grid: Array<IntArray>, row: Int, column: Int, number: Int): Boolean
}