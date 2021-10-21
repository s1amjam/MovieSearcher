package com.moviesearcher.favorite.tv.model

import com.google.gson.annotations.SerializedName

data class FavoriteTvResponse(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: MutableList<ResultFavoriteTv>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)