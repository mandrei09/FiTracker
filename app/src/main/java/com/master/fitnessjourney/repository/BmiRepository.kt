package com.master.fitnessjourney.repository

import com.master.fitnessjourney.entities.BmiEntry
import com.master.fitnessjourney.tasks.InsertBmiTask

object BmiRepository {
    fun insertBmi(entry: BmiEntry, onSuccess: () -> Unit) {
        InsertBmiTask(entry, onSuccess).execute()
    }
}

