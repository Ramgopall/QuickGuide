package com.duke.diskoverusosyal.model

import com.google.gson.annotations.SerializedName

data class CheckReferCodeModel(

    @field:SerializedName("exp")
	val exp: Any? = null,

    @field:SerializedName("detail")
	val detail: Any? = null,

    @field:SerializedName("skill")
	val skill: Any? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Boolean? = null
)
