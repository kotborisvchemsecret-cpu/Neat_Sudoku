package com.example.neatsudoku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.neatsudoku.ui.navigation.AppNavigation
import com.example.neatsudoku.ui.theme.NeatSudokuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NeatSudokuTheme() {
                AppNavigation()
            }
        }

    }
}

