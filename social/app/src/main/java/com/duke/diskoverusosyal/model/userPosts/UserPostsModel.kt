package com.duke.diskoverusosyal.model.userPosts

import com.google.gson.annotations.SerializedName

data class UserPostsModel(

	@field:SerializedName("response")
	val response: Response? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)