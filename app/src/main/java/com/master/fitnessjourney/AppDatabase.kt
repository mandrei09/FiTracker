package com.master.fitnessjourney

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.master.fitnessjourney.dao.ExerciceDao
import com.master.fitnessjourney.dao.ExerciceProgressDao
import com.master.fitnessjourney.dao.ProgressDao
import com.master.fitnessjourney.dao.UserDao
import com.master.fitnessjourney.dao.BmiDao
import com.master.fitnessjourney.entities.UserEntity
import com.master.fitnessjourney.entities.Exercice
import com.master.fitnessjourney.entities.ExerciceProgress
import com.master.fitnessjourney.entities.Progress
import com.master.fitnessjourney.entities.BmiEntry

@Database(
    entities = [
        UserEntity::class,
        Exercice::class,
        ExerciceProgress::class,
        Progress::class,
        BmiEntry::class
    ],
    version = 15
)
@TypeConverters(RoomConvertors::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun bmiDao(): BmiDao
    abstract val exerciceDao: ExerciceDao
    abstract val progressDao: ProgressDao
    abstract val excProgressDao: ExerciceProgressDao
}