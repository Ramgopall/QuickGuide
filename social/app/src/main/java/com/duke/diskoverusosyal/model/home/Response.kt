package com.duke.diskoverusosyal.model.home

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("data")
	val data: ArrayList<DataItem?>? = null
)