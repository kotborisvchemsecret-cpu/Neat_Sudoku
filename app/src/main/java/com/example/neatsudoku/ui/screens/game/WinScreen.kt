
package com.example.neatsudoku.ui.screens.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WinScreen(
    onPlayAgainClick: () -> Unit,
    onMainMenuClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🎉 Sudoku Solved!",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Congratulations! You completed the puzzle."
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onPlayAgainClick) {
            Text("Play Again")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onMainMenuClick) {
            Text("Main Menu")
        }
    }
}