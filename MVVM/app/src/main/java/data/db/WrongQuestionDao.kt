package com.triviallanguages.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.triviallanguages.data.db.entities.*

@Dao
interface WrongQuestionDao{

    @Insert
    suspend fun insertQuestionId(wrongQuestions: WrongQuestions)

    @Query("SELECT COUNT(questionId) FROM wrongQuestions WHERE questionId = :id")
    suspend fun getQuestionIdCount(id:String) : Int

    //room count query android
}