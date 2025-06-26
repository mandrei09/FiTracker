package com.master.fitnessjourney.tasks

import android.os.AsyncTask
import com.master.fitnessjourney.ApplicationController
import com.master.fitnessjourney.entities.BmiEntry

class InsertBmiTask(
    private val entry: BmiEntry,
    private val onSuccess: () -> Unit
) : AsyncTask<Unit, Unit, Unit>() {

    override fun doInBackground(vararg params: Unit?): Unit? {
        ApplicationController.instance?.appDatabase?.bmiDao()?.insert(entry)
        return null
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
        onSuccess.invoke()
    }
}
