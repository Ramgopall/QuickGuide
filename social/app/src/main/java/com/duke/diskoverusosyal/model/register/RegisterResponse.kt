package com.duke.diskoverusosyal.model.register

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("reffrel")
	val reffrel: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("url")
	val photo: String? = null,

	@field:SerializedName("last_id")
	val last_id: String? = null,

	@field:SerializedName("sess")
	val sess: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)