package com.master.fitnessjourney.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.master.fitnessjourney.entities.DifficultyExercicesEnum
import com.master.fitnessjourney.entities.Exercice
import com.master.fitnessjourney.entities.MuscleExercicesEnum
import com.master.fitnessjourney.entities.TypeExercicesEnum
import com.master.fitnessjourney.models.ExerciceModel

@Dao
public interface ExerciceDao {
    @Insert
    fun insertExercice(model: Exercice)

    @Query("DELETE FROM exercices WHERE id = :id")
    fun deleteExerciceById(id: Int)

    @Query("SELECT * FROM exercices")
    fun getAllExc(): List<Exercice>

    @Query("SELECT * FROM exercices WHERE type = :type AND difficulty = :difficulty AND muscle = :muscle")
    fun getAllExcByTypeDiffMuscle(type: TypeExercicesEnum, difficulty: DifficultyExercicesEnum, muscle: MuscleExercicesEnum): List<Exercice>

    @Query("SELECT * FROM exercices WHERE id = :id")
    fun getExcById(id: Int): Exercice?

    @Query("SELECT COUNT(*) FROM exercices WHERE name = :name and type = :type and muscle = :muscle and" +
            " equipment = :equipment and difficulty = :difficulty and instructions = :instructions")
    fun getExcByProperties(
        name: String, type: TypeExercicesEnum, muscle: MuscleExercicesEnum, equipment:String,
        difficulty: DifficultyExercicesEnum, instructions:String): Int

    @Query("SELECT id FROM exercices WHERE name = :name")
    fun getIdByName(name: String) :Int


}