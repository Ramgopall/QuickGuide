package com.duke.diskoverusosyal.model.comments

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class Response(

	@field:SerializedName("data")
	val data: ArrayList<DataItem>? = null
)