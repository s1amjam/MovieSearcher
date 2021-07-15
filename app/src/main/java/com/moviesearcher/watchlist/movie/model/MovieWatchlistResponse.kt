package com.moviesearcher.watchlist.movie.model

import com.google.gson.annotations.SerializedName
import com.moviesearcher.watchlist.tv.model.MovieWatchlistResult

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