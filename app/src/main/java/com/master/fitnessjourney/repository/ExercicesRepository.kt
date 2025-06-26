package com.master.fitnessjourney.repository

import com.master.fitnessjourney.entities.DifficultyExercicesEnum
import com.master.fitnessjourney.entities.Exercice
import com.master.fitnessjourney.entities.MuscleExercicesEnum
import com.master.fitnessjourney.entities.TypeExercicesEnum
import com.master.fitnessjourney.helpers.extensions.logErrorMessage
import com.master.fitnessjourney.models.ExerciceInProgress
import com.master.fitnessjourney.models.ExerciceModel
import com.master.fitnessjourney.tasks.GetAllDoneExercicesByUserTask
import com.master.fitnessjourney.tasks.GetAllDoneExercicesChoosedDateTask
import com.master.fitnessjourney.tasks.GetAllExcByTypeDiffMuscleTask
import com.master.fitnessjourney.tasks.GetAllExercicesInProgressTask
import com.master.fitnessjourney.tasks.GetExcByIdTask
import com.master.fitnessjourney.tasks.GetExcByPropertiesTask
import com.master.fitnessjourney.tasks.GetExerciceTask
import com.master.fitnessjourney.tasks.GetIdByNameTask
import com.master.fitnessjourney.tasks.InsertExerciceTask
import java.util.Date

object ExercicesRepository {//nu e un best practice...
    fun insertExercice(model: ExerciceModel, onSuccess : () -> Unit){
        InsertExerciceTask(onSuccess).execute(model)
    }

    fun getAllExc(callback: (List<Exercice>) -> Unit){
        GetExerciceTask{exercices ->
            callback(exercices)
        }.execute()
    }
    fun getExcById(id: Int){
        GetExcByIdTask{exercice ->
            if (exercice != null) {
                "exercice: ${exercice.name}".logErrorMessage()
            }
        }.execute(id)
    }
    fun getExcByProperties(ex: ExerciceModel,onSuccess: () -> Unit){
        GetExcByPropertiesTask(ex){response ->
            if (response == false)
            {
                insertExercice(ex,onSuccess)
            }

        }.execute()
    }

    fun getIdByName(name:String){
        GetIdByNameTask(name){

        }.execute()
    }
    fun getAllExcByTypeDiffMuscle(type: TypeExercicesEnum, difficulty: DifficultyExercicesEnum, muscle: MuscleExercicesEnum){
        GetAllExcByTypeDiffMuscleTask(type,difficulty,muscle){ exercices ->
            "listSucces: ${exercices.map { it.id }}".logErrorMessage()
        }.execute()
    }
    fun getExcDoneByDateUsername(day:String,month:String,year:String, username: String, callback: (List<ExerciceModel>) -> Unit){
        GetAllDoneExercicesChoosedDateTask(day,month,year,username){exerc->
            callback(exerc)
        }.execute()
    }

    fun getExcDoneByUsername(username: String, callback: (List<ExerciceModel>) -> Unit){
        GetAllDoneExercicesByUserTask(username){exerc->
            callback(exerc)
        }.execute()
    }
}
