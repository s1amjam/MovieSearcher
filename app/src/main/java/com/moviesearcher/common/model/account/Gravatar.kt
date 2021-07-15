package com.moviesearcher.common.model.account

import com.google.gson.annotations.SerializedName

data class Gravatar(
    @SerializedName("hash")
    val hash: String?
)