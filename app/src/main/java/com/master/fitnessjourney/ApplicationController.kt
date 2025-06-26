package com.master.fitnessjourney

import android.app.Application
import androidx.room.Room

class ApplicationController: Application() {
    lateinit var appDatabase: AppDatabase
    companion object{
        var instance: ApplicationController? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        appDatabase = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "Fitness_DB"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}