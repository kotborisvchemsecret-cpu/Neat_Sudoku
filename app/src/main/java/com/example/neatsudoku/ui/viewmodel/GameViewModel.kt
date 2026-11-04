package com.example.neatsudoku.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.neatsudoku.data.repository.GameProgressRepository
import com.example.neatsudoku.domain.model.Difficulty
import com.example.neatsudoku.domain.service.SudokuGameService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val gameService = SudokuGameService()
    private val progressRepository = GameProgressRepository(application)

    private val _uiState = MutableStateFlow(
        progressRepository.load() ?: GameUiState()
    )

    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private fun updateState(newState: GameUiState) {
        _uiState.value = newState
        progressRepository.save(newState)
    }

    fun startNewGame(difficulty: Difficulty = Difficulty.EASY) {
        val game = gameService.createNewGame(difficulty)

        updateState(
            GameUiState(
                game = game,
                difficulty = difficulty
            )
        )
    }

    fun ensureGameExists() {
        if (_uiState.value.game == null) {
            startNewGame()
        }
    }

    fun selectCell(row: Int, column: Int) {
        val state = _uiState.value

        if (state.isWon || state.isGameOver) return

        updateState(
            state.copy(
                selectedRow = row,
                selectedColumn = column
            )
        )
    }

    fun setCellValue(
        row: Int,
        column: Int,
        value: Int,
        autoRemovePencilNotes: Boolean = false
    ) {
        val state = _uiState.value
        val game = state.game ?: return

        if (state.isWon || state.isGameOver) return

        val cell = game.board.cells[row][column]
        val correctValue = game.solution.cells[row][column].value

        if (cell.isGiven || cell.value == correctValue) return

        if ((row to column) in state.incorrectCells) {
            if (state.isPencilMode || cell.value != value) return
        }

        val updatedValue = if (
            !state.isPencilMode && cell.value == value
        ) {
            null
        } else {
            value
        }

        val isCorrect = updatedValue == null ||
                updatedValue == correctValue

        var updatedCells = game.board.cells.mapIndexed { r, cells ->
            cells.mapIndexed { c, currentCell ->
                if (r == row && c == column) {
                    if (state.isPencilMode) {
                        val marks = currentCell.pencilMarks.toMutableSet()

                        if (value in marks) {
                            marks.remove(value)
                        } else {
                            marks.add(value)
                        }

                        currentCell.copy(pencilMarks = marks)
                    } else {
                        currentCell.copy(
                            value = updatedValue,
                            pencilMarks = emptySet()
                        )
                    }
                } else {
                    currentCell
                }
            }
        }

        // Remove the entered number from pencil notes in its
        // row, column and 3x3 box.
        if (
            autoRemovePencilNotes &&
            !state.isPencilMode &&
            updatedValue != null
        ) {
            updatedCells = updatedCells.mapIndexed { r, cells ->
                cells.mapIndexed { c, currentCell ->
                    val isSameCell = r == row && c == column

                    val isSameRow = r == row
                    val isSameColumn = c == column

                    val isSameBox =
                        r / 3 == row / 3 &&
                                c / 3 == column / 3

                    if (
                        !isSameCell &&
                        currentCell.value == null &&
                        (isSameRow || isSameColumn || isSameBox)
                    ) {
                        currentCell.copy(
                            pencilMarks = currentCell.pencilMarks - updatedValue
                        )
                    } else {
                        currentCell
                    }
                }
            }
        }

        val updatedBoard = game.board.copy(cells = updatedCells)
        val updatedIncorrectCells = state.incorrectCells.toMutableSet()

        var updatedMistakes = state.mistakes

        if (!state.isPencilMode) {
            if (isCorrect) {
                updatedIncorrectCells.remove(row to column)
            } else {
                updatedIncorrectCells.add(row to column)
                updatedMistakes++
            }
        }

        val isGameOver =
            updatedMistakes > state.difficulty.allowedMistakes

        val isWon = !isGameOver &&
                updatedBoard.cells.flatten().all { currentCell ->
                    currentCell.value ==
                            game.solution.cells[currentCell.row][currentCell.column].value
                }

        updateState(
            state.copy(
                game = game.copy(board = updatedBoard),
                incorrectCells = updatedIncorrectCells,
                mistakes = updatedMistakes,
                isWon = isWon,
                isGameOver = isGameOver
            )
        )
    }

    fun togglePencilMode() {
        val state = _uiState.value

        if (state.isWon || state.isGameOver) return

        updateState(
            state.copy(
                isPencilMode = !state.isPencilMode
            )
        )
    }
}