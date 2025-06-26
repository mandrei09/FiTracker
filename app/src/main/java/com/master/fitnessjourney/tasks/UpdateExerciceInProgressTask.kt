package com.master.fitnessjourney.tasks

import android.os.AsyncTask
import com.master.fitnessjourney.ApplicationController
import com.master.fitnessjourney.entities.ExerciceProgress

class UpdateExerciceInProgressTask(
    private val exercice: ExerciceProgress,
    private val onSuccess: () -> Unit
) : AsyncTask<Unit, Unit, Unit>() {
    override fun doInBackground(vararg params: Unit?) {
        ApplicationController.instance?.appDatabase?.excProgressDao?.updateExcProgress(exercice)
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
        onSuccess.invoke()
    }
}