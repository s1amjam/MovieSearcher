package com.moviesearcher.api.entity.common

import com.google.gson.annotations.SerializedName

data class ResponseWithCodeAndMessage(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("status_message")
    val statusMessage: String
)