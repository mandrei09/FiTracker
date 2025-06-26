package com.master.fitnessjourney.models

import com.master.fitnessjourney.entities.TypeExercicesEnum

data class CountTypeExcModel (
    val countExcProgress: Int,
    val typeExc: TypeExercicesEnum
)