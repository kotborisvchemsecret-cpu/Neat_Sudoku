package com.example.neatsudoku.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val playerColorOptions = listOf(
    Color(0xFF3F51B5),
    Color(0xFF008577),
    Color(0xFF7B1FA2),
    Color(0xFF1976D2),
    Color(0xFF388E3C),
    Color(0xFFE65100)
)

private val incorrectColorOptions = listOf(
    Color(0xFFF44336),
    Color(0xFFD81B60),
    Color(0xFF8E0000),
    Color(0xFFFF6F00),
    Color(0xFF6A1B9A)
)

@Composable
fun SettingsScreen(
    playerNumberColor: Color,
    incorrectNumberColor: Color,
    onPlayerColorChange: (Color) -> Unit,
    onIncorrectColorChange: (Color) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Entered number color",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        ColorOptions(
            colors = playerColorOptions,
            selectedColor = playerNumberColor,
            onColorSelected = onPlayerColorChange
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Incorrect number color",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        ColorOptions(
            colors = incorrectColorOptions,
            selectedColor = incorrectNumberColor,
            onColorSelected = onIncorrectColorChange
        )
    }
}

@Composable
private fun ColorOptions(
    colors: List<Color>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        colors.forEach { color ->
            Spacer(
                modifier = Modifier
                    .size(36.dp)
                    .background(color, CircleShape)
                    .then(
                        if (color == selectedColor) {
                            Modifier.border(
                                width = 3.dp,
                                color = Color.Black,
                                shape = CircleShape
                            )
                        } else {
                            Modifier
                        }
                    )
                    .clickable {
                        onColorSelected(color)
                    }
            )
        }
    }
}