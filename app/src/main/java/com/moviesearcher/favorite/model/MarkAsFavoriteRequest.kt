package com.moviesearcher.favorite.model

import com.google.gson.annotations.SerializedName

data class MarkAsFavoriteRequest(
    @SerializedName("favorite")
    val favorite: Boolean?,
    @SerializedName("media_id")
    val mediaId: Long?,
    @SerializedName("media_type")
    val mediaType: String?
)