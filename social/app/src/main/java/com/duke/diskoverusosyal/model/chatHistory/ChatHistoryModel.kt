package com.duke.diskoverusosyal.model.chatHistory

import com.duke.diskoverusosyal.model.Message
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class ChatHistoryModel(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("response")
	val response: ArrayList<Message>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)