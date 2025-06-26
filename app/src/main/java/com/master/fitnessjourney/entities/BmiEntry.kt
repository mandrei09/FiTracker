package com.master.fitnessjourney.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "bmi_entries",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["email"],
            childColumns = ["userEmail"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userEmail"])]
)

data class BmiEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userEmail: String,
    val weight: Float,
    val height: Float,
    val bmi: Float,
    val age: Int,
    val goal: String,
    val sex: String,
    val calories: Float,
    val activityFactor: Float,
    val date: Long = System.currentTimeMillis()
) {
    constructor(
        userEmail: String,
        weight: Float,
        height: Float,
        bmi: Float,
        age: Int,
        goal: String,
        sex: String,
        calories: Float,
        activityFactor: Float
    ) : this(
        0, userEmail, weight, height, bmi, age, goal, sex, calories, activityFactor, System.currentTimeMillis()
    )
}
