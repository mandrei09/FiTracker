package com.master.fitnessjourney.tasks

import android.os.AsyncTask
import com.master.fitnessjourney.ApplicationController
import com.master.fitnessjourney.entities.DifficultyExercicesEnum
import com.master.fitnessjourney.entities.Exercice
import com.master.fitnessjourney.entities.MuscleExercicesEnum
import com.master.fitnessjourney.entities.TypeExercicesEnum
import com.master.fitnessjourney.models.ExerciceModel

class InsertExerciceTask(
    val onSuccess: () -> Unit
): AsyncTask<ExerciceModel,Unit,Unit>() {
    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: ExerciceModel) {

        params.getOrNull(0)?.let { exercice ->
            val exerciceType = when (exercice.type.toUpperCase()) {
                "CARDIO" -> TypeExercicesEnum.CARDIO
                "OLYMPIC WEIGHTLIFTING" -> TypeExercicesEnum.OLYMPIC_WEIGHTLIFTING
                "PLYOMETRICS" -> TypeExercicesEnum.PLYOMETRICS
                "POWERLIFTING" -> TypeExercicesEnum.POWERLIFTING
                "STRENGTH" -> TypeExercicesEnum.STRENGTH
                "STRETCHING" -> TypeExercicesEnum.STRETCHING
                "STRONGMAN" -> TypeExercicesEnum.STRONGMAN
                else -> TypeExercicesEnum.CARDIO
            }
            val exerciceMuscle = when (exercice.muscle.toUpperCase()) {
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
            val exerciceDiff = when (exercice.difficulty.toUpperCase()) {
                "BEGINNER" -> DifficultyExercicesEnum.BEGINNER
                "INTERMEDIATE" -> DifficultyExercicesEnum.INTERMEDIATE
                "EXPERT" -> DifficultyExercicesEnum.EXPERT
                else -> DifficultyExercicesEnum.INTERMEDIATE
            }
            val exc = Exercice(
                name = exercice.name,
                type = exerciceType,
                muscle = exerciceMuscle,
                equipment = exercice.equipment,
                difficulty = exerciceDiff,
                instructions = exercice.instructions
            )
            ApplicationController
                .instance?.appDatabase?.exerciceDao?.insertExercice(exc)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: Unit) {
        super.onPostExecute(result)
        onSuccess.invoke()
    }
}