package com.duke.diskoverusosyal.model.wallet

import com.google.gson.annotations.SerializedName

data class WalletModel(

	@field:SerializedName("total")
	val total: String? = null,

	@field:SerializedName("min")
	val min: String? = null,

	@field:SerializedName("max")
	val max: String? = null,

	@field:SerializedName("wallet")
	val wallet: Wallet? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)