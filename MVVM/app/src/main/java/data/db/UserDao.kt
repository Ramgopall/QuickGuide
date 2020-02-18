package com.triviallanguages.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.triviallanguages.Constant.Companion.CURRENT_USER_ID
import com.triviallanguages.data.db.entities.User

@Dao
interface UserDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: User) : Long

    @Query("SELECT * FROM user WHERE uid = $CURRENT_USER_ID")
    fun getuser() : LiveData<User>

    @Query("SELECT token FROM user WHERE uid = $CURRENT_USER_ID")
    fun getToken() : String?

    @Query("SELECT _id FROM user WHERE uid = $CURRENT_USER_ID")
    fun getUserId() : String?

    @Query("SELECT _id FROM user WHERE uid = $CURRENT_USER_ID")
    suspend fun getUsersId() : String?

    @Query("SELECT level FROM user WHERE uid = $CURRENT_USER_ID")
    fun getUserLevel() : String?

    @Query("SELECT level FROM user WHERE uid = $CURRENT_USER_ID")
    suspend fun getUsersLevel() : String?

    @Query("UPDATE user SET level=:level WHERE uid = $CURRENT_USER_ID")
    suspend fun updateLevel(level:String)

}