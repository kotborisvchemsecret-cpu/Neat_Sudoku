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

        _uiState.value = GameUiState(
            game = game
        )
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
        val currentState = _uiState.value
        val currentGame = currentState.game ?: return
        val currentCell = currentGame.board.cells[row][column]
        val correctValue = currentGame.solution.cells[row][column].value

        if (currentCell.isGiven) {
            return
        }
        if (currentCell.value == correctValue) {
            return
        }
        if ((row to column) in currentState.incorrectCells) {
            if (currentState.isPencilMode || currentCell.value != value) {
                return
            }
        }

        val isPencilMode = currentState.isPencilMode

        val updatedValue = if (
            !isPencilMode && currentCell.value == value
        ) {
            null
        } else {
            value
        }


        val isCorrect = updatedValue == null || updatedValue == correctValue

        val updatedCells = currentGame.board.cells.mapIndexed { currentRow, cells ->
            cells.mapIndexed { currentColumn, cell ->
                if (currentRow == row && currentColumn == column) {
                    if (isPencilMode) {
                        val updatedPencilMarks = cell.pencilMarks.toMutableSet()

                        if (value in updatedPencilMarks) {
                            updatedPencilMarks.remove(value)
                        } else {
                            updatedPencilMarks.add(value)
                        }

                        cell.copy(pencilMarks = updatedPencilMarks)
                    } else {
                        cell.copy(
                            value = updatedValue,
                            pencilMarks = emptySet()
                        )
                    }
                } else {
                    cell
                }
            }
        }

        val updatedBoard = currentGame.board.copy(
            cells = updatedCells
        )

        val updatedIncorrectCells =
            currentState.incorrectCells.toMutableSet()

        if (!isPencilMode) {
            if (isCorrect) {
                updatedIncorrectCells.remove(row to column)
            } else {
                updatedIncorrectCells.add(row to column)
            }
        }

        val isWon = updatedBoard.cells.all { rowCells ->
            rowCells.all { cell ->
                cell.value != null
            }
        } && updatedBoard.cells.flatten().all { cell ->
            cell.value == currentGame.solution.cells[cell.row][cell.column].value
        }

        _uiState.value = currentState.copy(
            game = currentGame.copy(
                board = updatedBoard
            ),
            incorrectCells = updatedIncorrectCells,
            isWon = isWon
        )
    }

    fun togglePencilMode() {
        _uiState.value = _uiState.value.copy(
            isPencilMode = !_uiState.value.isPencilMode
        )
    }
}