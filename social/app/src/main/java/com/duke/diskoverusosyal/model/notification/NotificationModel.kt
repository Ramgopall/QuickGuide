package com.duke.diskoverusosyal.model.notification

import com.google.gson.annotations.SerializedName

data class NotificationModel(

	@field:SerializedName("response")
	val response: Response? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)