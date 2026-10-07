package com.example.neatsudoku.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.neatsudoku.domain.model.Difficulty
import com.example.neatsudoku.domain.service.SudokuGameService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel() : ViewModel() {
    private val gameService: SudokuGameService = SudokuGameService()
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun startNewGame(difficulty: Difficulty = Difficulty.EASY) {
        val game = gameService.createNewGame(difficulty)

        _uiState.value = _uiState.value.copy(game = game)
    }
    fun selectCell(row: Int, column: Int) {
        _uiState.value = _uiState.value.copy(
            selectedRow = row,
            selectedColumn = column
        )
    }

    fun setCellValue(
        row: Int,
        column: Int,
        value: Int
    ) {
        val currentGame = _uiState.value.game
            ?: return

        val currentCell = currentGame.board.cells[row][column]

        if (currentCell.isGiven) {
            return
        }
        val correctValue = currentGame.solution.cells[row][column].value
        val isCorrect = value == correctValue

        val updatedCells = currentGame.board.cells.mapIndexed { currentRow, cells ->
            cells.mapIndexed { currentColumn, cell ->
                if (currentRow == row && currentColumn == column) {
                    cell.copy(value = value)
                } else {
                    cell
                }
            }
        }

        val updatedBoard = currentGame.board.copy(
            cells = updatedCells
        )
        val updatedIncorrectCells =
            _uiState.value.incorrectCells.toMutableSet()

        if (isCorrect) {
            updatedIncorrectCells.remove(row to column)
        } else {
            updatedIncorrectCells.add(row to column)
        }

        _uiState.value = _uiState.value.copy(
            game = currentGame.copy(
                board = updatedBoard
            ),
            incorrectCells = updatedIncorrectCells
        )
    }
}