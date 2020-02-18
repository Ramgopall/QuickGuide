package com.duke.diskoverusosyal.model.forgot

import com.google.gson.annotations.SerializedName

data class ForgotPasswordModel(

	@field:SerializedName("response")
	val response: List<ResponseItem?>? = null
)