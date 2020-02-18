package com.duke.diskoverusosyal.model.wallet

import com.google.gson.annotations.SerializedName

data class DataItem(

	@field:SerializedName("reason")
	val reason: String? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("c_or_d")
	val cOrD: String? = null,

	@field:SerializedName("created_date")
	val createdDate: String? = null
)