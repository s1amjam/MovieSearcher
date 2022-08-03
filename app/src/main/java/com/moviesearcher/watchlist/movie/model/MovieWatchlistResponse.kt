package com.moviesearcher.watchlist.movie.model

import com.google.gson.annotations.SerializedName
import com.moviesearcher.watchlist.tv.model.MovieWatchlistResult

data class MovieWatchlistResponse(
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("results")
    var results: List<MovieWatchlistResult>? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null
)