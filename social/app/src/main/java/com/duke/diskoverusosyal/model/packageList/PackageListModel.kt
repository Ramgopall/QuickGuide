package com.duke.diskoverusosyal.model.packageList

import com.google.gson.annotations.SerializedName

data class PackageListModel(

	@field:SerializedName("response")
	val response: Response? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("banner")
	val banner: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)