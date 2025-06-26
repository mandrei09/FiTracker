package com.master.fitnessjourney.tasks

import android.os.AsyncTask
import com.master.fitnessjourney.ApplicationController
import com.master.fitnessjourney.entities.DifficultyExercicesEnum
import com.master.fitnessjourney.entities.Exercice
import com.master.fitnessjourney.entities.ExerciceProgress
import com.master.fitnessjourney.entities.MuscleExercicesEnum
import com.master.fitnessjourney.entities.TypeExercicesEnum
import com.master.fitnessjourney.models.ExerciceModel

class InsertExcProgressTask (
    val onSuccess: () -> Unit
): AsyncTask<ExerciceProgress, Unit, Unit>() {
    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: ExerciceProgress) {

        params.getOrNull(0)?.let { excprogress ->
            ApplicationController
                .instance?.appDatabase?.excProgressDao?.insertExcProgress(excprogress)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: Unit) {
        super.onPostExecute(result)
        onSuccess.invoke()
    }
}