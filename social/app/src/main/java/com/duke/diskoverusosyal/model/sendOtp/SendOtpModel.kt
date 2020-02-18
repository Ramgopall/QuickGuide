package com.duke.diskoverusosyal.model.sendOtp

import com.google.gson.annotations.SerializedName

data class SendOtpModel(

	@field:SerializedName("response")
	val response: List<SendOtpResponse?>? = null
)