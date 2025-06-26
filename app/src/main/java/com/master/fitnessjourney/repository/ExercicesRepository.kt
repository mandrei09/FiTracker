package com.master.fitnessjourney.repository

import android.content.Context
import com.master.fitnessjourney.entities.DifficultyExercicesEnum
import com.master.fitnessjourney.entities.Exercice
import com.master.fitnessjourney.entities.MuscleExercicesEnum
import com.master.fitnessjourney.entities.TypeExercicesEnum
import com.master.fitnessjourney.helpers.extensions.logErrorMessage
import com.master.fitnessjourney.models.ExerciceInProgress
import com.master.fitnessjourney.models.ExerciceModel
import com.master.fitnessjourney.tasks.*

object ExercicesRepository {

    fun insertExercice(context: Context, model: ExerciceModel, onSuccess: () -> Unit) {
        InsertExerciceTask(context, onSuccess).execute(model)
    }

    fun getAllExc(callback: (List<Exercice>) -> Unit) {
        GetExerciceTask { exercices ->
            callback(exercices)
        }.execute()
    }

    fun getExcById(id: Int) {
        GetExcByIdTask { exercice ->
            exercice?.let {
                "exercice: ${it.name}".logErrorMessage()
            }
        }.execute(id)
    }

    fun getExcByProperties(context: Context, ex: ExerciceModel, onSuccess: () -> Unit) {
        GetExcByPropertiesTask(ex) { response ->
            if (response == false) {
                insertExercice(context, ex, onSuccess)
            }
        }.execute()
    }

    fun getIdByName(name: String) {
        GetIdByNameTask(name) {
            // handle response if needed
        }.execute()
    }

    fun getAllExcByTypeDiffMuscle(
        type: TypeExercicesEnum,
        difficulty: DifficultyExercicesEnum,
        muscle: MuscleExercicesEnum
    ) {
        GetAllExcByTypeDiffMuscleTask(type, difficulty, muscle) { exercices ->
            "listSucces: ${exercices.map { it.id }}".logErrorMessage()
        }.execute()
    }

    fun getExcDoneByDateUsername(
        day: String,
        month: String,
        year: String,
        username: String,
        callback: (List<ExerciceModel>) -> Unit
    ) {
        GetAllDoneExercicesChoosedDateTask(day, month, year, username) { exerc ->
            callback(exerc)
        }.execute()
    }

    fun getExcDoneByUsername(username: String, callback: (List<ExerciceModel>) -> Unit) {
        GetAllDoneExercicesByUserTask(username) { exerc ->
            callback(exerc)
        }.execute()
    }
}
