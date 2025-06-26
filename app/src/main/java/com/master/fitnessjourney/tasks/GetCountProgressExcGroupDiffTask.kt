package com.master.fitnessjourney.tasks

import android.os.AsyncTask
import com.master.fitnessjourney.ApplicationController
import com.master.fitnessjourney.models.CountDiffExcModel
import com.master.fitnessjourney.models.CountTypeExcModel

class GetCountProgressExcGroupDiffTask(val username: String, val onSuccess: (List<CountDiffExcModel>) -> Unit):
    AsyncTask<Unit, Unit, List<CountDiffExcModel>>() {
    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Unit): List<CountDiffExcModel> {
        return ApplicationController.instance?.appDatabase?.excProgressDao?.getCountProgressExcGroupDiffByUser(username) ?: listOf()
    }
    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: List<CountDiffExcModel>) {
        super.onPostExecute(result)
        onSuccess.invoke(result);
    }
}