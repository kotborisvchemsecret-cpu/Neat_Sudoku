
package com.example.neatsudoku.ui.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.neatsudoku.domain.model.SudokuBoard
import com.example.neatsudoku.ui.theme.SudokuColors
import com.example.neatsudoku.ui.viewmodel.GameViewModel

@Composable
fun GameScreen(
    onWin: () -> Unit,
    viewModel: GameViewModel = viewModel()
) {
    val sudokuColors = SudokuColors(
        selectedCell = Color.LightGray,
        selectedRowColumn = Color(0xFFE8E8E8),
        gridLine = Color.Gray,
        boxLine = Color.Black,
        givenNumber = Color.Black,
        playerNumber = Color(0xFF3F51B5),
        incorrectNumber = Color.Red
    )

    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(uiState.isWon) {
        if (uiState.isWon) {
            onWin()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.startNewGame()
    }

    val game = uiState.game

    if (game == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Creating Game")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sudoku",
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.size(24.dp))

        SudokuGrid(
            board = game.board,
            selectedRow = uiState.selectedRow,
            selectedColumn = uiState.selectedColumn,
            incorrectCells = uiState.incorrectCells,
            colors = sudokuColors,
            onCellClick = { row, column ->
                viewModel.selectCell(row, column)
            }
        )

        Spacer(modifier = Modifier.size(24.dp))

        Button(
            onClick = {
                viewModel.togglePencilMode()
            }
        ) {
            Text(
                text = if (uiState.isPencilMode) {
                    "Pencil: ON"
                } else {
                    "Pencil: OFF"
                }
            )
        }

        Spacer(modifier = Modifier.size(8.dp))

        NumberPad(
            onNumberClick = { number ->
                val row = uiState.selectedRow
                val column = uiState.selectedColumn

                if (row != null && column != null) {
                    viewModel.setCellValue(
                        row = row,
                        column = column,
                        value = number
                    )
                }
            }
        )
    }
}

@Composable
private fun SudokuCellView(
    value: Int?,
    pencilMarks: Set<Int>,
    isSameValue: Boolean,
    isGiven: Boolean,
    isSelected: Boolean,
    isInSelectedRow: Boolean,
    isInSelectedColumn: Boolean,
    isIncorrect: Boolean,
    colors: SudokuColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val numberColor = when {
        isIncorrect -> colors.incorrectNumber
        isGiven -> colors.givenNumber
        else -> colors.playerNumber
    }

    val backgroundModifier = when {
        isSelected -> Modifier.background(colors.selectedCell)
        isInSelectedRow || isInSelectedColumn ->
            Modifier.background(colors.selectedRowColumn)
        isSameValue -> Modifier.background(Color(0xFFEBEBEB))
        else -> Modifier
    }

    val density = LocalDensity.current

    val mainNumberSize = with(density) {
        (24.sp.toPx() / fontScale).toSp()
    }

    val pencilNumberSize = with(density) {
        (9.sp.toPx() / fontScale).toSp()
    }

    Box(
        modifier = modifier
            .then(backgroundModifier)
            .border(0.5.dp, colors.gridLine)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (value != null) {
            Text(
                text = value.toString(),
                color = numberColor,
                fontSize = mainNumberSize,
                fontWeight = if (isSameValue) {
                    FontWeight.Bold
                } else {
                    FontWeight.Normal
                }
            )
        } else if (pencilMarks.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                for (row in 0 until 3) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        for (column in 0 until 3) {
                            val number = row * 3 + column + 1

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (number in pencilMarks) {
                                    Text(
                                        text = number.toString(),
                                        fontSize = pencilNumberSize,
                                        lineHeight = pencilNumberSize
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SudokuGrid(
    board: SudokuBoard,
    selectedRow: Int?,
    selectedColumn: Int?,
    incorrectCells: Set<Pair<Int, Int>>,
    colors: SudokuColors,
    onCellClick: (Int, Int) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        val cellSize = maxWidth / 9

        val selectedValue =
            if (selectedRow != null && selectedColumn != null) {
                board.cells[selectedRow][selectedColumn].value
            } else {
                null
            }

        for (row in 0 until 9) {
            for (column in 0 until 9) {
                val cell = board.cells[row][column]

                SudokuCellView(
                    value = cell.value,
                    pencilMarks = cell.pencilMarks,
                    isSameValue = cell.value != null &&
                            cell.value == selectedValue,
                    isGiven = cell.isGiven,
                    isSelected = row == selectedRow &&
                            column == selectedColumn,
                    isInSelectedRow = row == selectedRow,
                    isInSelectedColumn = column == selectedColumn,
                    isIncorrect = (row to column) in incorrectCells,
                    colors = colors,
                    onClick = {
                        onCellClick(row, column)
                    },
                    modifier = Modifier
                        .size(cellSize)
                        .offset(
                            x = cellSize * column,
                            y = cellSize * row
                        )
                )
            }
        }

        // Draw the thick 3x3 boundaries above all cell backgrounds.
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val cellWidth = size.width / 9f
                    val cellHeight = size.height / 9f

                    for (line in 1..2) {
                        val x = cellWidth * line * 3

                        drawLine(
                            color = colors.boxLine,
                            start = Offset(x, 0f),
                            end = Offset(x, size.height),
                            strokeWidth = 3.dp.toPx()
                        )
                    }

                    for (line in 1..2) {
                        val y = cellHeight * line * 3

                        drawLine(
                            color = colors.boxLine,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 3.dp.toPx()
                        )
                    }
                }
        )

        // Draw the outer border last so it stays consistently black.
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .border(2.dp, Color.Black)
        )
    }
}

@Composable
private fun NumberPad(
    onNumberClick: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (row in 0 until 3) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (column in 1..3) {
                    val number = row * 3 + column

                    Button(
                        onClick = {
                            onNumberClick(number)
                        }
                    ) {
                        Text(
                            text = number.toString(),
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}
