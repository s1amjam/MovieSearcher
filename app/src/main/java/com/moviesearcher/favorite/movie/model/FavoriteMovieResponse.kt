package com.moviesearcher.favorite.movie.model

import com.google.gson.annotations.SerializedName

data class FavoriteMovieResponse(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: MutableList<ResultFavoriteMovie>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)