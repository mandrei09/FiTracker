package com.master.fitnessjourney.repository

import com.master.fitnessjourney.ApplicationController
import com.master.fitnessjourney.entities.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UserRepository {

    private val userDao = ApplicationController.instance!!.appDatabase.userDao()

    suspend fun getByEmail(email: String): UserEntity? {
        return withContext(Dispatchers.IO) {
            userDao.getUserByEmail(email)
        }
    }

    suspend fun register(user: UserEntity) {
        withContext(Dispatchers.IO) {
            userDao.insert(user)
        }
    }

    suspend fun login(username: String, password: String): UserEntity? {
        return withContext(Dispatchers.IO) {
            userDao.login(username, password)
        }
    }
    suspend fun deleteByEmail(email: String) {
        withContext(Dispatchers.IO) {
            userDao.deleteByEmail(email)
        }
    }

}
