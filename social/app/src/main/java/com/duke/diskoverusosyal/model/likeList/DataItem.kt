package com.duke.diskoverusosyal.model.likeList

import com.google.gson.annotations.SerializedName

data class DataItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("sess")
	val sess: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)