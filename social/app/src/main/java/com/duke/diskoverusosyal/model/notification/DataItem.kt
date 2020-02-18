package com.duke.diskoverusosyal.model.notification

import com.google.gson.annotations.SerializedName

data class DataItem(

	@field:SerializedName("notification")
	val notification: String? = null,

	@field:SerializedName("nid")
	val nid: String? = null,

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("pid")
	val pid: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("seen")
	val seen: String? = null
)