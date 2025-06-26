package com.master.fitnessjourney.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.master.fitnessjourney.entities.BmiEntry

@Dao
interface BmiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: BmiEntry)

    @Query("SELECT * FROM bmi_entries WHERE userEmail = :email ORDER BY date DESC")
    fun getAll(email: String): LiveData<List<BmiEntry>>

    @Query("SELECT * FROM bmi_entries WHERE userEmail = :email ORDER BY date DESC LIMIT 1")
    fun getLastEntry(email: String): BmiEntry?

    @Query("SELECT * FROM bmi_entries WHERE userEmail = :email ORDER BY date DESC")
    suspend fun getAllSuspend(email: String): List<BmiEntry>
}
