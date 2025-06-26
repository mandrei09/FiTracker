package com.master.fitnessjourney.tasks

import android.os.AsyncTask
import com.master.fitnessjourney.ApplicationController
import java.util.Date

class IsProgrExcTask(
    val progressId:Int,
    val exerciceId: Int,
    val onSuccess: (Boolean) -> Unit): AsyncTask<Unit, Unit, Boolean>() {
    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Unit): Boolean {

        val progressExcExist =
            ApplicationController.instance?.appDatabase?.excProgressDao?.isProgrExc(
                progressId,exerciceId)
        if (progressExcExist == 0)
            return false //it doesn't exist
        else
            return true
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        onSuccess.invoke(result)
    }
}