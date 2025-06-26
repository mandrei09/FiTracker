package com.master.fitnessjourney

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.master.fitnessjourney.dao.ExerciceDao
import com.master.fitnessjourney.dao.ExerciceProgressDao
import com.master.fitnessjourney.dao.ProgressDao
import com.master.fitnessjourney.entities.Exercice
import com.master.fitnessjourney.entities.ExerciceProgress
import com.master.fitnessjourney.entities.Progress

@Database(
    entities = [
        Exercice::class,
    ExerciceProgress::class,
    Progress::class
    ],
    version = 6
)
@TypeConverters(RoomConvertors::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val exerciceDao: ExerciceDao
    abstract val progressDao: ProgressDao
    abstract val excProgressDao: ExerciceProgressDao
}