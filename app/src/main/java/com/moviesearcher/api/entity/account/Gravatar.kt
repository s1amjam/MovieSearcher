package com.moviesearcher.api.entity.account

import com.google.gson.annotations.SerializedName

data class Gravatar(
    @SerializedName("hash")
    val hash: String?
)