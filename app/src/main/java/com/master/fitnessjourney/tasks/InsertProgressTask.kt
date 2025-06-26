package com.master.fitnessjourney.tasks

import android.os.AsyncTask
import com.master.fitnessjourney.ApplicationController
import com.master.fitnessjourney.entities.DifficultyExercicesEnum
import com.master.fitnessjourney.entities.Exercice
import com.master.fitnessjourney.entities.MuscleExercicesEnum
import com.master.fitnessjourney.entities.Progress
import com.master.fitnessjourney.entities.TypeExercicesEnum
import com.master.fitnessjourney.models.ExerciceModel

class InsertProgressTask (val onSuccess: () -> Unit
): AsyncTask<String, Unit, Unit>() {
    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: String) {

        params.getOrNull(0)?.let { name ->
            val progr = Progress(username = name)
            ApplicationController
                .instance?.appDatabase?.progressDao?.insertProgress(progr)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: Unit) {
        super.onPostExecute(result)
        onSuccess.invoke()
    }
}