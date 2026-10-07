package com.example.neatsudoku.ui.screens.game

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.neatsudoku.domain.model.SudokuBoard
import com.example.neatsudoku.ui.navigation.Screen
import com.example.neatsudoku.ui.viewmodel.GameViewModel
import androidx.compose.foundation.background
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.draw.drawBehind
import com.example.neatsudoku.ui.theme.SudokuColors
import androidx.compose.ui.text.font.FontWeight

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel()
){
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
    LaunchedEffect(Unit) {
        viewModel.startNewGame()
    }
    val game = uiState.game

    if (game == null){
        Box(Modifier.fillMaxSize(), Alignment.Center){
         Text("Creating Game")
        }
        return
    }
    Column(Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text("Sudoku", fontSize = 32.sp)

        Spacer(modifier = Modifier.size(24.dp))

        SudokuGrid(
            board = game.board,
            selectedRow = uiState.selectedRow,
            selectedColumn = uiState.selectedColumn,
            incorrectCells = uiState.incorrectCells,
            colors = sudokuColors,
            onCellClick = {
                row, column -> viewModel.selectCell(row,column)
            }
        )
        Spacer(modifier = Modifier.size(24.dp))

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
    isSameValue: Boolean,
    isGiven: Boolean,
    isSelected: Boolean,
    isInSelectedRow: Boolean,
    isInSelectedColumn: Boolean,
    isIncorrect: Boolean,
    isRightBoxEdge: Boolean,
    isBottomBoxEdge: Boolean,
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
        isSelected -> Modifier.background(Color.LightGray)
        isInSelectedRow || isInSelectedColumn ->
            Modifier.background(Color(0xFFE8E8E8))
        isSameValue -> Modifier.background(Color(0xFFEBEBEB))
        else -> Modifier
    }

    Box(
        modifier = modifier
            .then(backgroundModifier)
            .border(0.5.dp, Color.Gray)
            .drawBehind {
                if (isRightBoxEdge) {
                    drawLine(
                        color = Color.Black,
                        start = Offset(size.width, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = 3.dp.toPx()
                    )
                }

                if (isBottomBoxEdge) {
                    drawLine(
                        color = Color.Black,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 3.dp.toPx()
                    )
                }
            }
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (value != null) {
            Text(
                text = value.toString(),
                color = numberColor,
                fontSize = 24.sp,
                fontWeight = if (isSameValue) {
                    FontWeight.Bold
                } else {
                    FontWeight.Normal
                }
            )
        }
    }
}

@Composable
private fun SudokuGrid(
    board: SudokuBoard,
    selectedRow: Int?,
    selectedColumn: Int?,
    incorrectCells: Set<Pair<Int,Int>>,
    colors: SudokuColors,
    onCellClick: (Int, Int) -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .border(2.dp, Color.Black)
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
                    isSameValue = cell.value != null &&
                            cell.value == selectedValue,
                    isGiven = cell.isGiven,
                    isSelected = row == selectedRow &&
                            column == selectedColumn,
                    isInSelectedRow = row == selectedRow,
                    isInSelectedColumn = column == selectedColumn,
                    isIncorrect = (row to column) in incorrectCells,
                    isRightBoxEdge = column == 2 || column == 5 || column == 8,
                    isBottomBoxEdge = row == 2 || row == 5 || row == 8,
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
    }
}

@Composable
private fun NumberPad(onNumberClick: (Int) -> Unit
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