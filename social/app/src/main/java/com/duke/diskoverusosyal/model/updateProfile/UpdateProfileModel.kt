package com.duke.diskoverusosyal.model.updateProfile

import com.google.gson.annotations.SerializedName

data class UpdateProfileModel(

	@field:SerializedName("response")
	val response: List<UpdateProfileResponse?>? = null
)