package com.duke.diskoverusosyal.model.refer

import com.google.gson.annotations.SerializedName

data class ReferModel(

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("reffrel")
	val reffrel: String? = null,

	@field:SerializedName("text")
	val text: String? = null
)