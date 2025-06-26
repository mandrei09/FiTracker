package com.master.fitnessjourney.repository

import com.master.fitnessjourney.entities.Progress
import com.master.fitnessjourney.models.ExerciceModel
import com.master.fitnessjourney.tasks.GetExcByPropertiesTask
import com.master.fitnessjourney.tasks.GetIdByNameTask
import com.master.fitnessjourney.tasks.GetIdByUserDateTask
import com.master.fitnessjourney.tasks.InsertExerciceTask
import com.master.fitnessjourney.tasks.InsertProgressTask
import com.master.fitnessjourney.tasks.IsProgressSetTodayUserLoggedTask

object ProgressRepository {
    fun insertProgress(username: String, onSuccess : () -> Unit){
        InsertProgressTask(onSuccess).execute(username)
    }
    fun isProgressTodayUserLogged(username: String, onSuccess: () -> Unit){
        IsProgressSetTodayUserLoggedTask(username){response ->
            if (response == false)
            {
                insertProgress(username,onSuccess)
            }

        }.execute()
    }

    fun getIdByUserDate(username:String){
        GetIdByUserDateTask(username){

        }.execute()
    }
}