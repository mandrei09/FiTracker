package com.master.fitnessjourney.tasks

import android.os.AsyncTask
import com.master.fitnessjourney.ApplicationController
import com.master.fitnessjourney.entities.Exercice
import com.master.fitnessjourney.models.ExerciceInProgress

class GetAllExercicesInProgressTask(
    val name: String,
    val onSuccess: (List<ExerciceInProgress>) -> Unit
): AsyncTask<Unit, Unit, List<ExerciceInProgress>>() {
    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Unit): List<ExerciceInProgress>{
        return ApplicationController.instance?.appDatabase?.
        excProgressDao?.getAllExercicesInProgress(name) ?: listOf()
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: List<ExerciceInProgress>) {
        super.onPostExecute(result)
        onSuccess.invoke(result)
    }}