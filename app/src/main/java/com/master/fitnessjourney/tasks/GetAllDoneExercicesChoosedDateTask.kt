package com.master.fitnessjourney.tasks

import android.os.AsyncTask
import com.master.fitnessjourney.ApplicationController
import com.master.fitnessjourney.models.ExerciceInProgress
import com.master.fitnessjourney.models.ExerciceModel
import java.util.Date

class GetAllDoneExercicesChoosedDateTask(
   val day: String,
   val month: String,
   val year: String,
    val username: String,
    val onSuccess: (List<ExerciceModel>) -> Unit
): AsyncTask<Unit, Unit, List<ExerciceModel>>() {
    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Unit): List<ExerciceModel>{
        return ApplicationController.instance?.appDatabase?.
        excProgressDao?.getAllDoneExercicesChoosedDate(day,month,year,username) ?: listOf()
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: List<ExerciceModel>) {
        super.onPostExecute(result)
        onSuccess.invoke(result)
    }}