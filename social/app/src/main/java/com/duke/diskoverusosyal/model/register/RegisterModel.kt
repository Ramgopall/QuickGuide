package com.duke.diskoverusosyal.model.register

import com.google.gson.annotations.SerializedName

data class RegisterModel(

	@field:SerializedName("response")
	val response: List<RegisterResponse?>? = null
)