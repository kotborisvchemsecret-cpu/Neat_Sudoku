package com.example.neatsudoku.data.repository

import android.content.Context
import com.example.neatsudoku.domain.model.SudokuBoard
import com.example.neatsudoku.domain.model.SudokuCell
import com.example.neatsudoku.domain.model.SudokuGame
import com.example.neatsudoku.domain.model.Difficulty
import com.example.neatsudoku.ui.viewmodel.GameUiState
import org.json.JSONArray
import org.json.JSONObject

class GameProgressRepository(context: Context) {

    private val preferences = context.applicationContext.getSharedPreferences(
        "sudoku_progress",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val GAME_KEY = "active_game"
    }

    fun save(state: GameUiState) {
        val game = state.game ?: return

        val json = JSONObject().apply {
            put("puzzle", boardToJson(game.puzzle))
            put("board", boardToJson(game.board))
            put("solution", boardToJson(game.solution))

            put("difficulty", state.difficulty.name)
            put("mistakes", state.mistakes)
            put("isWon", state.isWon)
            put("isGameOver", state.isGameOver)
            put("isPencilMode", state.isPencilMode)

            put(
                "selectedRow",
                state.selectedRow ?: JSONObject.NULL
            )
            put(
                "selectedColumn",
                state.selectedColumn ?: JSONObject.NULL
            )

            put("incorrectCells", JSONArray().apply {
                state.incorrectCells.forEach { (row, column) ->
                    put(
                        JSONArray().put(row).put(column)
                    )
                }
            })
        }

        preferences.edit()
            .putString(GAME_KEY, json.toString())
            .apply()
    }

    fun load(): GameUiState? {
        val savedJson = preferences.getString(GAME_KEY, null)
            ?: return null

        return runCatching {
            val json = JSONObject(savedJson)

            val game = SudokuGame(
                puzzle = boardFromJson(json.getJSONObject("puzzle")),
                board = boardFromJson(json.getJSONObject("board")),
                solution = boardFromJson(json.getJSONObject("solution"))
            )

            val incorrectCells = mutableSetOf<Pair<Int, Int>>()
            val incorrectArray = json.getJSONArray("incorrectCells")

            for (index in 0 until incorrectArray.length()) {
                val coordinates = incorrectArray.getJSONArray(index)

                incorrectCells.add(
                    coordinates.getInt(0) to coordinates.getInt(1)
                )
            }

            GameUiState(
                game = game,
                difficulty = Difficulty.valueOf(
                    json.optString("difficulty", Difficulty.EASY.name)
                ),
                mistakes = json.optInt("mistakes", 0),
                isWon = json.optBoolean("isWon", false),
                isGameOver = json.optBoolean("isGameOver", false),
                isPencilMode = json.optBoolean("isPencilMode", false),
                selectedRow = if (json.isNull("selectedRow")) {
                    null
                } else {
                    json.getInt("selectedRow")
                },
                selectedColumn = if (json.isNull("selectedColumn")) {
                    null
                } else {
                    json.getInt("selectedColumn")
                },
                incorrectCells = incorrectCells
            )
        }.getOrNull()
    }

    private fun boardToJson(board: SudokuBoard): JSONObject {
        val rows = JSONArray()

        board.cells.forEach { row ->
            val cells = JSONArray()

            row.forEach { cell ->
                cells.put(
                    JSONObject().apply {
                        put(
                            "value",
                            cell.value ?: JSONObject.NULL
                        )
                        put("isGiven", cell.isGiven)
                        put("row", cell.row)
                        put("column", cell.column)
                        put(
                            "pencilMarks",
                            JSONArray(cell.pencilMarks.toList())
                        )
                    }
                )
            }

            rows.put(cells)
        }

        return JSONObject().put("cells", rows)
    }

    private fun boardFromJson(json: JSONObject): SudokuBoard {
        val rows = json.getJSONArray("cells")

        val cells = List(rows.length()) { rowIndex ->
            val row = rows.getJSONArray(rowIndex)

            List(row.length()) { columnIndex ->
                val cell = row.getJSONObject(columnIndex)
                val pencilMarksJson = cell.getJSONArray("pencilMarks")

                val pencilMarks = buildSet {
                    for (index in 0 until pencilMarksJson.length()) {
                        add(pencilMarksJson.getInt(index))
                    }
                }

                SudokuCell(
                    value = if (cell.isNull("value")) {
                        null
                    } else {
                        cell.getInt("value")
                    },
                    isGiven = cell.getBoolean("isGiven"),
                    row = cell.getInt("row"),
                    column = cell.getInt("column"),
                    pencilMarks = pencilMarks
                )
            }
        }

        return SudokuBoard(cells)
    }
}