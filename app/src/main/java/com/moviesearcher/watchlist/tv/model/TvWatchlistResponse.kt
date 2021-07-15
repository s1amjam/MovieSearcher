package com.moviesearcher.watchlist.tv.model

import com.google.gson.annotations.SerializedName

data class TvWatchlistResponse(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: List<TvWatchlistResult>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)