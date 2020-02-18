package com.duke.diskoverusosyal.model.home

import com.google.gson.annotations.SerializedName

data class DataItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("sess")
	val sess: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("ownlike")
	var ownlike: String? = null,

	@field:SerializedName("totallike")
	var totallike: String? = null,

	@field:SerializedName("totalcoment")
	var totalcoment: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("desc")
	val desc: String? = null,

	@field:SerializedName("share_url")
	val share_url: String? = null
)