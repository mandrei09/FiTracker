package com.master.fitnessjourney.tasks

import android.os.AsyncTask
import com.master.fitnessjourney.ApplicationController

class GetIdByNameTask(
    val name: String,
    val onSuccess: (Int) -> Unit
): AsyncTask<Unit, Unit, Int>() {
    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Unit): Int? {
        return ApplicationController.instance?.appDatabase?.
        exerciceDao?.getIdByName(name)
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: Int) {
        super.onPostExecute(result)
        onSuccess.invoke(result)
    }

}