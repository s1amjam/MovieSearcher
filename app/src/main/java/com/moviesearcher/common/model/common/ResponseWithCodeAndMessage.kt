package com.moviesearcher.common.model.common

import com.google.gson.annotations.SerializedName

data class ResponseWithCodeAndMessage(
    @SerializedName("success")
    val success: Boolean = false,
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("status_message")
    val statusMessage: String
)