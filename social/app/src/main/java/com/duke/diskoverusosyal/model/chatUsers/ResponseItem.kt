package com.duke.diskoverusosyal.model.chatUsers

import com.google.gson.annotations.SerializedName

data class ResponseItem(

	@field:SerializedName("invitationid")
	val invitationid: String? = null,

	@field:SerializedName("sess")
	val sess: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("updateddate")
	val updateddate: String? = null,

	@field:SerializedName("seen")
	val seen: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)