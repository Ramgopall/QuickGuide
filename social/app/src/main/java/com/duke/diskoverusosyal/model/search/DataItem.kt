package com.duke.diskoverusosyal.model.search

import com.google.gson.annotations.SerializedName

data class DataItem(

	@field:SerializedName("sess")
	val sess: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)