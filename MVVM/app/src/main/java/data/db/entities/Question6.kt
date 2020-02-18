package com.triviallanguages.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Question6(
    var _id: String? = null,
    var question: String? = null,
    var correct: String? = null,
    var wrong1: String? = null,
    var wrong2: String? = null,
    var wrong3: String? = null,
    var category: String? = null,
    var level: String? = null,
    var priority: String? = null
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "user_answer")
    var user_answer: String = ""

    @ColumnInfo(name = "answer_correct")
    var answer_correct : String = ""
}