package com.duke.diskoverusosyal.model.packageList

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null
)