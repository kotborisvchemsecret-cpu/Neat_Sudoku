package com.example.neatsudoku.ui.screens.difficulty

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.neatsudoku.domain.model.Difficulty

@Composable
fun DifficultySelectionScreen(
    onStartGame: (Difficulty) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedDifficulty by remember {
        mutableStateOf(Difficulty.EASY)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Select Difficulty",
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Difficulty.entries.forEach { difficulty ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedDifficulty == difficulty,
                    onClick = {
                        selectedDifficulty = difficulty
                    }
                )

                Text(
                    text = difficulty.name.lowercase()
                        .replaceFirstChar { it.uppercase() },
                    fontSize = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                onStartGame(selectedDifficulty)
            }
        ) {
            Text("Start Game")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onBackClick
        ) {
            Text("Back")
        }
    }
}