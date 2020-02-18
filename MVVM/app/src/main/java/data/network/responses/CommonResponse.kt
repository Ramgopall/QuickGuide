package com.xyz.data.network.responses

import com.xyz.data.db.entities.User


data class CommonResponse(
    val message: String?,
    val users: User?
)