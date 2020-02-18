package com.duke.diskoverusosyal.model.likeList

import com.google.gson.annotations.SerializedName

data class LikeListModel(

	@field:SerializedName("response")
	val response: Response? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)