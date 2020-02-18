package com.duke.diskoverusosyal.model.packageList

import com.google.gson.annotations.SerializedName

data class DataItem(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("vid")
	val vid: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("days")
	val days: String? = null
)