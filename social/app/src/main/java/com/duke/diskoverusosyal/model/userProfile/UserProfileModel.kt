package com.duke.diskoverusosyal.model.userProfile

import com.google.gson.annotations.SerializedName

data class UserProfileModel(

	@field:SerializedName("exp")
	val exp: Any? = null,

	@field:SerializedName("detail")
	val detail: Any? = null,

	@field:SerializedName("skill")
	val skill: Any? = null,

	@field:SerializedName("totalpost")
	val totalpost: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("chatstatus")
	val chatstatus: String? = null,

	@field:SerializedName("wallet")
	val wallet: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("totallike")
	val totallike: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)