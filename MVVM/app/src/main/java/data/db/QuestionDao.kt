package com.triviallanguages.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.triviallanguages.data.db.entities.*

@Dao
interface QuestionDao{

    @Insert
    suspend fun insertQuestion1(question1: List<Question1>)

    @Insert
    suspend fun insertQuestion2(question2: List<Question2>)

    @Insert
    suspend fun insertQuestion3(question3: List<Question3>)

    @Query("DELETE FROM question1")
    suspend fun deleteQuestion1()

    @Query("DELETE FROM question2")
    suspend fun deleteQuestion2()

    @Query("DELETE FROM question3")
    suspend fun deleteQuestion3()


    @Query("SELECT * FROM question1")
    fun getQuestion1() : LiveData<List<Question1>>

    @Query("SELECT * FROM question2")
    fun getQuestion2() : LiveData<List<Question1>>

    @Query("SELECT * FROM question3")
    fun getQuestion3() : LiveData<List<Question1>>


}