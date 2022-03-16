package com.moviesearcher.favorite.movie.model

import com.google.gson.annotations.SerializedName

data class FavoriteMovieResponse(
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("results")
    var results: MutableList<ResultFavoriteMovie>? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null
)