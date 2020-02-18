package com.duke.diskoverusosyal.model.CommonResponse

import com.google.gson.annotations.SerializedName

data class CommonResponseModel(

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
)