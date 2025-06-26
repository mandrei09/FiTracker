package com.master.fitnessjourney.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.master.fitnessjourney.entities.DifficultyExercicesEnum
import com.master.fitnessjourney.entities.Exercice
import com.master.fitnessjourney.entities.ExerciceProgress
import com.master.fitnessjourney.entities.MuscleExercicesEnum
import com.master.fitnessjourney.entities.Progress
import com.master.fitnessjourney.entities.TypeExercicesEnum
import com.master.fitnessjourney.models.CountDiffExcModel
import com.master.fitnessjourney.models.CountTypeExcModel
import com.master.fitnessjourney.models.ExerciceInProgress
import com.master.fitnessjourney.models.ExerciceModel
import java.util.Date

@Dao
interface ExerciceProgressDao {
    @Insert
    fun insertExcProgress(model: ExerciceProgress)

    @Query("SELECT COUNT(*) FROM exercices_progesses WHERE progressId = :progressId and exerciceId = :exerciceId")
    fun isProgrExc(progressId:Int,exerciceId:Int): Int

    @Query("SELECT exercices.id 'exerciceId', progresses.id 'progressId',exercices.name, exercices.instructions, exercices.equipment FROM exercices " +
            "join exercices_progesses on(exercices.id == exercices_progesses.exerciceId)" +
            "join progresses on(exercices_progesses.progressId == progresses.id)" +
            " WHERE status = 0 AND username = :username")
    fun getAllExercicesInProgress(username: String): List<ExerciceInProgress>

    @Delete
    fun deleteExcProgress(model: ExerciceProgress)

    @Update
    fun updateExcProgress(model: ExerciceProgress)

    @Query("SELECT exercices.name, exercices.type, exercices.muscle, exercices.equipment, exercices.difficulty, exercices.instructions" +
            " FROM exercices" +
            " JOIN exercices_progesses on (exercices.id == exercices_progesses.exerciceId)" +
            " JOIN progresses on (exercices_progesses.progressId == progresses.id) " +
            "WHERE status = 1 AND username = :username AND " +
            " strftime('%d', datetime(date / 1000, 'unixepoch','localtime')) = :day AND" +
            " strftime('%m',datetime(date / 1000, 'unixepoch','localtime')) = :month AND" +
            " strftime('%Y', datetime(date / 1000, 'unixepoch','localtime')) = :year")
    fun getAllDoneExercicesChoosedDate(day:String,month: String, year: String, username: String): List<ExerciceModel>
    @Query("SELECT exercices.name, exercices.type, exercices.muscle, exercices.equipment, exercices.difficulty, exercices.instructions" +
            " FROM exercices" +
            " JOIN exercices_progesses on (exercices.id == exercices_progesses.exerciceId)" +
            " JOIN progresses on (exercices_progesses.progressId == progresses.id) " +
            "WHERE status = 1 AND username = :username")
    fun getAllDoneExercicesByUser(username: String): List<ExerciceModel>


    @Query("select count(*) 'countExcProgress',type 'typeExc' " +
            "from exercices_progesses join exercices on (exercices_progesses.exerciceId = exercices.id) " +
            "join progresses on (exercices_progesses.progressId = progresses.id) " +
            "where progresses.username = :username " +
            "group by type")
    fun getCountProgressExcGroupTypeByUser(username: String): List<CountTypeExcModel>

    @Query("select count(*) 'countExcProgress',difficulty " +
            "from exercices_progesses join exercices on (exercices_progesses.exerciceId = exercices.id) " +
            "join progresses on (exercices_progesses.progressId = progresses.id) " +
            "where progresses.username = :username " +
            "group by difficulty")
    fun getCountProgressExcGroupDiffByUser(username: String): List<CountDiffExcModel>

}