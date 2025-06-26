package com.master.fitnessjourney.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.master.fitnessjourney.entities.Exercice
import com.master.fitnessjourney.entities.Progress
import com.master.fitnessjourney.models.ExerciceByDateModel
import com.master.fitnessjourney.models.ExerciceInProgress
import com.master.fitnessjourney.models.ProgressWithExercices
import java.time.Year
import java.util.Date

@Dao
public interface ProgressDao {

    @Insert
    fun insertProgress(model: Progress)

    @Query("SELECT COUNT(*) FROM progresses WHERE username = :username AND strftime('%d', datetime(date / 1000, 'unixepoch','localtime')) = :day AND strftime('%m',datetime(date / 1000, 'unixepoch','localtime')) = :month AND strftime('%Y', datetime(date / 1000, 'unixepoch','localtime')) = :year")
    fun isProgressSetTodayUserLogged(username: String,day: String,month: String, year: String) : Int

    @Query("UPDATE exercices_progesses SET status= 1 WHERE exerciceId = :exerciceId AND progressId = :progressId")
    fun updateProgressStatus(exerciceId: Int, progressId: Int)

    @Query("DELETE FROM progresses WHERE id = :id")
    fun deleteProgress(id: Int)

    @Query("SELECT exercices.id 'exerciceId', name FROM exercices " +
            "JOIN exercices_progesses ON (exercices.id == exercices_progesses.exerciceId)" +
            "JOIN progresses ON (progresses.id == exercices_progesses.progressId)" +
            "WHERE status = 1 AND date = :chooseDate")
    fun getDoneExercicesByDate(chooseDate: Date): List<ExerciceByDateModel>

    @Query("SELECT id FROM progresses WHERE username = :username AND" +
            " strftime('%d', datetime(date / 1000, 'unixepoch','localtime')) = :day AND" +
            " strftime('%m',datetime(date / 1000, 'unixepoch','localtime')) = :month AND" +
            " strftime('%Y', datetime(date / 1000, 'unixepoch','localtime')) = :year")
    fun getIdByUserDate(username: String,day:String,month: String,year: String) :Int


}