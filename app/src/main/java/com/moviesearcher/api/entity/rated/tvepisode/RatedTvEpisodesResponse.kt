package com.moviesearcher.api.entity.rated.tvepisode

import com.google.gson.annotations.SerializedName

data class RatedTvEpisodesResponse(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: List<RatedTvEpisodeResult>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)