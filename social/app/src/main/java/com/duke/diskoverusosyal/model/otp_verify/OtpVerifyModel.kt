package com.duke.diskoverusosyal.model.otp_verify

import com.google.gson.annotations.SerializedName

data class OtpVerifyModel(

	@field:SerializedName("response")
	val response: List<OtpVerifyResponse?>? = null
)