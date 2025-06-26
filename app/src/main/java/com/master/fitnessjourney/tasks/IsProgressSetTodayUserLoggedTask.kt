package com.master.fitnessjourney.tasks

import android.os.AsyncTask
import com.master.fitnessjourney.ApplicationController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class IsProgressSetTodayUserLoggedTask (
    val username: String,
    val onSuccess: (Boolean) -> Unit): AsyncTask<Unit, Unit, Boolean>() {

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Unit): Boolean {
        val date = Date()
        val currentDate = formatDate(date)
        val componentsDate = currentDate.split("/")
        val progressExist =
            ApplicationController.instance?.appDatabase?.progressDao?.isProgressSetTodayUserLogged(
                username, componentsDate[0], componentsDate[1], componentsDate[2]
            )
        if (progressExist == 0)
            return false //it doesn't exist
        else
            return true
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        onSuccess.invoke(result)
    }
    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }
}