package com.duke.diskoverusosyal.model.bankInfo

import com.google.gson.annotations.SerializedName

data class BankInfoModel(

	@field:SerializedName("response")
	val response: Response? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)