package com.xyz.data.repositories

import com.xyz.data.db.AppDatabase
import com.xyz.data.db.entities.User
import com.xyz.data.network.MyApi
import com.xyz.data.network.SafeApiRequest
import com.xyz.data.network.responses.LoginResponse

class UserRepository(
    private val api: MyApi,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun userLogin(email: String, name: String, photo: String): LoginResponse {
        return apiRequest { api.userLogin(email, name, photo) }
    }

    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)

    fun getUser() = db.getUserDao().getuser()

    suspend fun getUserLevel() = db.getUserDao().getUsersLevel()
}