package com.duke.diskoverusosyal.model.userPosts

import com.google.gson.annotations.SerializedName

data class DataItem(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)