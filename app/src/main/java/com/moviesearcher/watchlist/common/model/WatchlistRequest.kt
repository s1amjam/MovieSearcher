package com.moviesearcher.watchlist.common.model

import com.google.gson.annotations.SerializedName

data class WatchlistRequest(
    @SerializedName("watchlist")
    val watchlist: Boolean?,
    @SerializedName("media_id")
    val mediaId: Long?,
    @SerializedName("media_type")
    val mediaType: String?
)