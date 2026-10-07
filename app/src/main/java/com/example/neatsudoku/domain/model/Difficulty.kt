package com.example.neatsudoku.domain.model

enum class Difficulty(
    val minimumClues: Int,
    val maximumClues: Int
) {
    EASY(40, 45),
    MEDIUM(32, 39),
    HARD(27, 31),
    EXPERT(22, 26)
}