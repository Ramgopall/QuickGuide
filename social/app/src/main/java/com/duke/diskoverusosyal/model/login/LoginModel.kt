package com.duke.diskoverusosyal.model.login

import com.google.gson.annotations.SerializedName

data class LoginModel(

	@field:SerializedName("response")
	val response: List<LoginResponse?>? = null
)