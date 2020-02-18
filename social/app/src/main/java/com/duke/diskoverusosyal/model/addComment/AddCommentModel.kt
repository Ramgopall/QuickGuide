package com.duke.diskoverusosyal.model.addComment

import com.google.gson.annotations.SerializedName

data class AddCommentModel(

	@field:SerializedName("totalcoment")
	val totalcoment: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)