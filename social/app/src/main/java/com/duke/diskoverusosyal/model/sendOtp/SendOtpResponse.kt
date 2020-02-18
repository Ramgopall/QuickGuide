package com.duke.diskoverusosyal.model.sendOtp

import com.google.gson.annotations.SerializedName

data class SendOtpResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)