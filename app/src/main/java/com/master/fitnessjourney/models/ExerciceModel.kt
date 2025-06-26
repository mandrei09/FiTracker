package com.master.fitnessjourney.models

import com.master.fitnessjourney.entities.TypeExercicesEnum

data class ExerciceModel (//cell model
    val name: String,
    val type: String,
    val muscle: String,
    val equipment: String,
    val difficulty: String,
    val instructions: String,

)