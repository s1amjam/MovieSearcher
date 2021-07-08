package com.moviesearcher.api.entity.list

import com.google.gson.annotations.SerializedName

data class RemoveFromListResponse(
    @SerializedName("status_code")
    val statusCode: Int?,
    @SerializedName("status_message")
    val statusMessage: String?
)