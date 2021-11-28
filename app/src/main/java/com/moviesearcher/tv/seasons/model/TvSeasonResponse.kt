package com.moviesearcher.tv.seasons.model

import com.google.gson.annotations.SerializedName

data class TvSeasonResponse(
    @SerializedName("_id")
    val _id: String?,
    @SerializedName("air_date")
    val airDate: String?,
    val episodes: List<Episode>?,
    val id: Int?,
    val name: String?,
    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("season_number")
    val seasonNumber: Int?
)