package com.moviesearcher.common.model.auth

import com.google.gson.annotations.SerializedName

data class CreateTokenResponse(
    @SerializedName("expires_at")
    val expiresAt: String? = null,
    @SerializedName("request_token")
    val requestToken: String? = null,
    @SerializedName("success")
    val success: Boolean? = null
)