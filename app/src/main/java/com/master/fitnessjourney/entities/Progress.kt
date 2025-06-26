package com.master.fitnessjourney.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "progresses")
data class Progress(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: Date = Date(),
    val username: String
){
    constructor(username: String) : this(0,Date(),username = username)
}
