package com.master.fitnessjourney.repository

import com.master.fitnessjourney.entities.Exercice
import com.master.fitnessjourney.entities.ExerciceProgress
import com.master.fitnessjourney.models.CountDiffExcModel
import com.master.fitnessjourney.models.CountTypeExcModel
import com.master.fitnessjourney.models.ExerciceInProgress
import com.master.fitnessjourney.tasks.DeleteExerciceInProgressTask
import com.master.fitnessjourney.tasks.GetAllExercicesInProgressTask
import com.master.fitnessjourney.tasks.GetCountProgressExcGroupDiffTask
import com.master.fitnessjourney.tasks.GetCountProgressExcGroupTypeTask
import com.master.fitnessjourney.tasks.GetExerciceTask
import com.master.fitnessjourney.tasks.GetIdByNameTask
import com.master.fitnessjourney.tasks.GetIdByUserDateTask
import com.master.fitnessjourney.tasks.InsertExcProgressTask
import com.master.fitnessjourney.tasks.InsertExerciceTask
import com.master.fitnessjourney.tasks.IsProgrExcTask
import com.master.fitnessjourney.tasks.IsProgressSetTodayUserLoggedTask
import com.master.fitnessjourney.tasks.UpdateExerciceInProgressTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ExcProgressRepository {
    var id1:Int=0;
    var id2:Int=0;
    fun insertExercProgress(model: ExerciceProgress, onSuccess : () -> Unit){
        InsertExcProgressTask(onSuccess).execute(model)
    }
    fun deleteExerciceInProgress(exercice: ExerciceInProgress, onSuccess: () -> Unit) {
        val excProgress = ExerciceProgress(
            progressId = exercice.progressId,
            exerciceId = exercice.exerciceId,
            status = false
        )
        DeleteExerciceInProgressTask(excProgress) {
            onSuccess()
        }.execute()
    }
    fun updateExerciceInProgress(exercice: ExerciceInProgress, onSuccess: () -> Unit) {
        val excProgress = ExerciceProgress(
            progressId = exercice.progressId,
            exerciceId = exercice.exerciceId,
            status = true
        )
        UpdateExerciceInProgressTask(excProgress) {
            onSuccess()
        }.execute()
    }
    fun uu(name:String,username:String){
        val resExc = GetIdByNameTask(name){}.execute().get()
        val idExc = resExc ?: 0

        val resProgr = GetIdByUserDateTask(username){}.execute().get()
        val idProgr = resProgr ?: 0
        val excProgress = ExerciceProgress(
            progressId = idProgr,
            exerciceId = idExc,
            status = false
        )
        if(excProgress.progressId!=0 && excProgress.exerciceId!=0)
        {
            isProgressExc(excProgress.progressId,excProgress.exerciceId){}
        }

    }

    fun isProgressExc(progressId: Int, exerciceId: Int, onSuccess: () -> Unit){
        IsProgrExcTask(progressId,exerciceId){response ->
            if (response == false)
            {
                val excProgress = ExerciceProgress(
                    progressId = progressId,
                    exerciceId = exerciceId,
                    status = false
                )
                insertExercProgress(excProgress){}
            }

        }.execute()
    }

    fun getExcProgress(username: String,callback: (List<ExerciceInProgress>) -> Unit){
        GetAllExercicesInProgressTask(username){exerc->
            callback(exerc)
        }.execute()
    }
    fun getCountExcProgressGroupType(callback: (List<CountTypeExcModel>) -> Unit){
        GetCountProgressExcGroupTypeTask{exCount ->
            callback(exCount)
        }.execute()
    }

    fun getCountExcProgressGroupDiff(callback: (List<CountDiffExcModel>) -> Unit){
        GetCountProgressExcGroupDiffTask{exCount ->
            callback(exCount)
        }.execute()
    }
}

