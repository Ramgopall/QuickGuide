package com.duke.diskoverusosyal.model.comments

import com.google.gson.annotations.SerializedName

data class CommentsModel(

	@field:SerializedName("response")
	val response: Response? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)