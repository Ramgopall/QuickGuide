package com.duke.diskoverusosyal.model.paymentToken

import com.google.gson.annotations.SerializedName

data class PayMentTokenModel(

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("cftoken")
	val cftoken: String? = null,

	@field:SerializedName("transection")
	val transection: String? = null
)