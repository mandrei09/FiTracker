package com.master.fitnessjourney.tasks

import android.os.AsyncTask
import com.master.fitnessjourney.ApplicationController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GetIdByUserDateTask(
    val username: String,
    val onSuccess: (Int) -> Unit): AsyncTask<Unit, Unit, Int>() {

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Unit): Int? {
        val date = Date()
        val currentDate = formatDate(date)
        val componentsDate = currentDate.split("/")
        return ApplicationController.instance?.appDatabase?.progressDao?.getIdByUserDate(
                username, componentsDate[0], componentsDate[1], componentsDate[2])

    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: Int) {
        super.onPostExecute(result)
        onSuccess.invoke(result)
    }
    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }
}