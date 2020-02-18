package com.duke.diskoverusosyal.model.home

import com.google.gson.annotations.SerializedName

data class HomeModel(

	@field:SerializedName("totalnotseen")
	val totalnotseen: String? = null,

	@field:SerializedName("response")
	val response: Response? = null,

	@field:SerializedName("totalpages")
	val totalpages: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("image")
	val bonanza: String? = null,

	@field:SerializedName("detail")
	val detail: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)