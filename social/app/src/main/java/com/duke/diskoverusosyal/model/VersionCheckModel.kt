package com.duke.diskoverusosyal.model

import com.google.gson.annotations.SerializedName

data class VersionCheckModel(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("vcode")
	val vcode: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)