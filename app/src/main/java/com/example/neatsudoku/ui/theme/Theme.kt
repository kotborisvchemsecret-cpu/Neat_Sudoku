package com.example.neatsudoku.ui.theme


import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier


private val DarkSudokuColors = SudokuColors(
    selectedCell = Color(0xFF221E2B),
    selectedRowColumn = Color(0xFF363040),
    gridLine = Color(0xFF31204F),
    boxLine = Color(0xFF402B63),
    givenNumber = Color.LightGray,
    playerNumber = Color(0xFF3F51B5),
    incorrectNumber = Color.Red,
    sameValueCell = Color(0xFF292232),
)

private val LightSudokuColors = SudokuColors(
    selectedCell = Color.LightGray,
    selectedRowColumn = Color(0xFFE8E8E8),
    gridLine = Color.Gray,
    boxLine = Color.Black,
    givenNumber = Color.Black,
    playerNumber = Color(0xFF3F51B5),
    incorrectNumber = Color.Red,
    sameValueCell = Color(0xFFEBEBEB),
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFF121212),
    surface = Color(0xFF121212)
)

private val LightColorScheme = lightColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF)
)
val LocalSudokuColors = staticCompositionLocalOf {
    LightSudokuColors
}
@Composable
fun NeatSudokuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val sudokuColors = if (darkTheme) {
        DarkSudokuColors
    } else {
        LightSudokuColors
    }

    CompositionLocalProvider(
        LocalSudokuColors provides sudokuColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                content()
            }
        }
    }
}