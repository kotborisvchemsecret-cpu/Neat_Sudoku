package com.example.neatsudoku.ui.screens.mainmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainMenuScreen(onNewGameClick: () -> Unit,
                   onContinueClick: () -> Unit,
                   onSettingsClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "SUDOKU",
            fontSize = 40.sp
        )

        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = onContinueClick
        ) {
            Text("Continue")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onNewGameClick
        ) {
            Text("New Game")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onSettingsClick
        ) {
            Text("Settings")
        }
    }
}