package com.moviesearcher.api.entity.rated.tv

import com.google.gson.annotations.SerializedName

data class RatedTvsResponse(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: List<RatedTvsResult>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)