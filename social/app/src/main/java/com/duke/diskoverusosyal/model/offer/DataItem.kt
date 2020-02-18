package com.duke.diskoverusosyal.model.offer

import com.google.gson.annotations.SerializedName

data class DataItem(

	@field:SerializedName("creationdate")
	val creationdate: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("dateto")
	val dateto: String? = null,

	@field:SerializedName("datefrom")
	val datefrom: String? = null,

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)