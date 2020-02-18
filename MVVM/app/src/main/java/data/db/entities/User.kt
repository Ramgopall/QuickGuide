package com.triviallanguages.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.triviallanguages.Constant.Companion.CURRENT_USER_ID

@Entity
data class User(
    var _id: String? = null,
    var full_name: String? = null,
    var email: String? = null,
    var photo: String? = null,
    var level: String? = null,
    var __v: Int? = null,
    var token: String? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null
){
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID
}
