package com.example.neatsudoku.domain.model

enum class Difficulty(
    val minimumClues: Int,
    val maximumClues: Int,
    val allowedMistakes: Int
) {
    EASY(40, 45, 5),
    MEDIUM(32, 39, 3),
    HARD(27, 31, 1),
    EXPERT(22, 26, 0)
}