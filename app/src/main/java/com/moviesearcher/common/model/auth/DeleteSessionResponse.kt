package com.moviesearcher.common.model.auth

import com.google.gson.annotations.SerializedName

data class DeleteSessionResponse(
    @SerializedName("success")
    val success: Boolean? = null
)