package com.master.fitnessjourney.models

import com.master.fitnessjourney.entities.DifficultyExercicesEnum
data class CountDiffExcModel (
    val countExcProgress: Int,
    val difficulty: DifficultyExercicesEnum
)