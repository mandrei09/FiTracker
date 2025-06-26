package com.master.fitnessjourney.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.master.fitnessjourney.entities.Exercice
import com.master.fitnessjourney.entities.ExerciceProgress
import com.master.fitnessjourney.entities.Progress

data class ProgressWithExercices (
    @Embedded val progress: Progress,
            @Relation(
                parentColumn = "progressId",
                entityColumn = "exerciceId",
                associateBy = Junction(ExerciceProgress::class)
            )
            val exercices: List<Exercice>
)
