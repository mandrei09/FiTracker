package com.master.fitnessjourney.tasks

import android.os.AsyncTask
import com.master.fitnessjourney.ApplicationController
import com.master.fitnessjourney.entities.Exercice

class GetExcByIdTask(val onSuccess: (Exercice?) -> Unit): AsyncTask<Int, Unit, Exercice?>() {
    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Int?): Exercice? {
        return params[0]?.let {
            ApplicationController.instance?.appDatabase?.exerciceDao?.getExcById(
                it
            )
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: Exercice?) {
        super.onPostExecute(result)
        onSuccess.invoke(result)
    }
}