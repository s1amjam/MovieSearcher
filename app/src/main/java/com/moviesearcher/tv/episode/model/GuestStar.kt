package com.moviesearcher.tv.episode.model

import com.google.gson.annotations.SerializedName

data class GuestStar(
    @SerializedName("character")
    val character: String?,
    @SerializedName("creditId")
    val credit_id: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("order")
    val order: Int?,
    @SerializedName("profilePath")
    val profile_path: String?
)