package com.moviesearcher.api.entity.favorites

import com.google.gson.annotations.SerializedName

data class FavoriteMovieResponse(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: List<ResultFavoriteMovie>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)