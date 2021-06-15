package com.moviesearcher.api.entity.auth

import com.google.gson.annotations.SerializedName

data class CreateSessionResponse(
    @SerializedName("session_id")
    val sessionId: String?,
    @SerializedName("success")
    val success: Boolean?
)