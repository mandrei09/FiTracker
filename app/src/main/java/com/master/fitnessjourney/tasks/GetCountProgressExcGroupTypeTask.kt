package com.master.fitnessjourney.tasks

import android.os.AsyncTask
import com.master.fitnessjourney.ApplicationController
import com.master.fitnessjourney.entities.Exercice
import com.master.fitnessjourney.models.CountTypeExcModel

class GetCountProgressExcGroupTypeTask(val username: String, val onSuccess: (List<CountTypeExcModel>) -> Unit):
    AsyncTask<Unit, Unit, List<CountTypeExcModel>>() {
    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Unit): List<CountTypeExcModel> {
        return ApplicationController.instance?.appDatabase?.excProgressDao?.getCountProgressExcGroupTypeByUser(username) ?: listOf()
    }
    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: List<CountTypeExcModel>) {
        super.onPostExecute(result)
        onSuccess.invoke(result);
    }
}