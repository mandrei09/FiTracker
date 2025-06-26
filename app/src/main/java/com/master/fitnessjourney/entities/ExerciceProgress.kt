package com.master.fitnessjourney.entities

import androidx.room.Entity
@Entity(tableName = "exercices_progesses", primaryKeys = ["progressId","exerciceId"] )
data class ExerciceProgress(
    val progressId: Int,
    val exerciceId: Int,
    val status: Boolean = false
) 