package com.moviesearcher.api.entity.list.add

import com.google.gson.annotations.SerializedName

data class AddToListResponse(
    @SerializedName("status_code")
    val statusCode: Int?,
    @SerializedName("status_message")
    val statusMessage: String?
)