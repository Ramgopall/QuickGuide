package com.duke.diskoverusosyal.model.otp_verify

import com.google.gson.annotations.SerializedName

data class OtpVerifyResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)