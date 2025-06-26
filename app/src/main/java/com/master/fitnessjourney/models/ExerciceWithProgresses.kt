package com.master.fitnessjourney.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.master.fitnessjourney.entities.Exercice
import com.master.fitnessjourney.entities.ExerciceProgress
import com.master.fitnessjourney.entities.Progress

class ExerciceWithProgresses (
    @Embedded val exercice: Exercice,
    @Relation(
        parentColumn = "exerciceId",
        entityColumn = "progressId",
        associateBy = Junction(ExerciceProgress::class)
    )
    val progress: List<Progress>
)