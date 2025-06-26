package com.master.fitnessjourney.dao

import androidx.room.*
import com.master.fitnessjourney.entities.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    fun login(username: String, password: String): UserEntity?

    @Query("DELETE FROM users WHERE email = :email")
    fun deleteByEmail(email: String)

}
