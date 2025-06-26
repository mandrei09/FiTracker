package com.master.fitnessjourney.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val email: String,
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String
)
