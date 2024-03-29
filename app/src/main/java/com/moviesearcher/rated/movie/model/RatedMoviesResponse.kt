package com.moviesearcher.rated.movie.model

import com.google.gson.annotations.SerializedName

data class RatedMoviesResponse(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: List<RatedMoviesResult>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)