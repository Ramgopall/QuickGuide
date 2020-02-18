package com.duke.diskoverusosyal.model.forgot

import com.google.gson.annotations.SerializedName

data class ResponseItem(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("msg")
	val msg: String? = null
)