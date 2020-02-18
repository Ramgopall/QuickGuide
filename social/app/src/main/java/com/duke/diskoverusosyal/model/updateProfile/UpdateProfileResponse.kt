package com.duke.diskoverusosyal.model.updateProfile

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)