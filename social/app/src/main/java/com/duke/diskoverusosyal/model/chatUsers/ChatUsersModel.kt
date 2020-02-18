package com.duke.diskoverusosyal.model.chatUsers

import com.google.gson.annotations.SerializedName

data class ChatUsersModel(

	@field:SerializedName("response")
	val response: List<ResponseItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)