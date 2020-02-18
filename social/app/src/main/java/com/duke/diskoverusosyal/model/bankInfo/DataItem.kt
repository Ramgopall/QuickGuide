package com.duke.diskoverusosyal.model.bankInfo

import com.google.gson.annotations.SerializedName

data class DataItem(

	@field:SerializedName("accountno")
	val accountno: String? = null,

	@field:SerializedName("bname")
	val bname: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("lastupdate")
	val lastupdate: String? = null,

	@field:SerializedName("ifsc")
	val ifsc: String? = null
)