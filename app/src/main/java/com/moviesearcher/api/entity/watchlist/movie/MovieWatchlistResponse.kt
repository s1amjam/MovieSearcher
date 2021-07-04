package com.moviesearcher.api.entity.watchlist.movie

import com.google.gson.annotations.SerializedName

data class MovieWatchlistResponse(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: List<MovieWatchlistResult>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)