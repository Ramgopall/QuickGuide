package com.duke.diskoverusosyal.model.wallet

import com.google.gson.annotations.SerializedName

data class Wallet(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null
)