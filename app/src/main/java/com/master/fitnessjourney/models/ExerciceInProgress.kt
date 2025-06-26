package com.master.fitnessjourney.models

data class ExerciceInProgress(
    val exerciceId: Int,
    val progressId: Int,
    val name: String,
    val instructions: String,
    val equipment: String,
    val videoUrl: String
)
