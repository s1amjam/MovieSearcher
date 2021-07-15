package com.moviesearcher.common.model.account

import com.google.gson.annotations.SerializedName

data class Avatar(
    @SerializedName("gravatar")
    val gravatar: Gravatar?
)