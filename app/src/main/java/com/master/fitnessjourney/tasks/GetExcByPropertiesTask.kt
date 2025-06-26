package com.master.fitnessjourney.tasks

import android.os.AsyncTask
import com.master.fitnessjourney.ApplicationController
import com.master.fitnessjourney.entities.DifficultyExercicesEnum
import com.master.fitnessjourney.entities.Exercice
import com.master.fitnessjourney.entities.MuscleExercicesEnum
import com.master.fitnessjourney.entities.TypeExercicesEnum
import com.master.fitnessjourney.models.ExerciceModel

class GetExcByPropertiesTask (
    val ex: ExerciceModel,
    val onSuccess: (Boolean) -> Unit): AsyncTask<Unit, Unit, Boolean>() {

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Unit): Boolean {
    val exerciceType = when (ex.type.toUpperCase()) {
        "CARDIO" -> TypeExercicesEnum.CARDIO
        "OLYMPIC WEIGHTLIFTING" -> TypeExercicesEnum.OLYMPIC_WEIGHTLIFTING
        "PLYOMETRICS" -> TypeExercicesEnum.PLYOMETRICS
        "POWERLIFTING" -> TypeExercicesEnum.POWERLIFTING
        "STRENGTH" -> TypeExercicesEnum.STRENGTH
        "STRETCHING" -> TypeExercicesEnum.STRETCHING
        "STRONGMAN" -> TypeExercicesEnum.STRONGMAN
        else -> TypeExercicesEnum.CARDIO
    }
    val exerciceMuscle = when (ex.muscle.toUpperCase()) {
        "ABDOMINALS" -> MuscleExercicesEnum.ABDOMINALS
        "ABDUCTORS" -> MuscleExercicesEnum.ABDUCTORS
        "ADDUCTORS" -> MuscleExercicesEnum.ADDUCTORS
        "BICEPS" -> MuscleExercicesEnum.BICEPS
        "CALVES" -> MuscleExercicesEnum.CALVES
        "CHEST" -> MuscleExercicesEnum.CHEST
        "FOREARMS" -> MuscleExercicesEnum.FOREARMS
        "GLUTES" -> MuscleExercicesEnum.GLUTES
        "HAMSTRINGS" -> MuscleExercicesEnum.HAMSTRINGS
        "LATS" -> MuscleExercicesEnum.LATS
        "LOWER_BACK" -> MuscleExercicesEnum.LOWER_BACK
        "MIDDLE_BACK" -> MuscleExercicesEnum.MIDDLE_BACK
        "NECK" -> MuscleExercicesEnum.NECK
        "QUADRICEPS" -> MuscleExercicesEnum.QUADRICEPS
        "TRAPS" -> MuscleExercicesEnum.TRAPS
        "TRICEPS" -> MuscleExercicesEnum.TRICEPS
        else -> MuscleExercicesEnum.ABDOMINALS
    }
    val exerciceDiff = when (ex.difficulty.toUpperCase()) {
        "BEGINNER" -> DifficultyExercicesEnum.BEGINNER
        "INTERMEDIATE" -> DifficultyExercicesEnum.INTERMEDIATE
        "EXPERT" -> DifficultyExercicesEnum.EXPERT
        else -> DifficultyExercicesEnum.INTERMEDIATE
    }
        val exerciseExist = ApplicationController.instance?.appDatabase?.exerciceDao?.getExcByProperties(
            ex.name, exerciceType, exerciceMuscle, ex.equipment, exerciceDiff, ex.instructions)
        if(exerciseExist == 0)
            return false
        else
            return true
    }
    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        onSuccess.invoke(result)
    }
}