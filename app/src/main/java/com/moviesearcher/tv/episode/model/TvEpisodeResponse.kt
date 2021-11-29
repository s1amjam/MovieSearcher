package com.moviesearcher.tv.episode.model

import com.google.gson.annotations.SerializedName
import java.math.RoundingMode

data class TvEpisodeResponse(
    @SerializedName("air_date")
    val airDate: String?,
    @SerializedName("crew")
    var crew: List<Crew>?,
    @SerializedName("episode_number")
    val episodeNumber: Int?,
    @SerializedName("guest_stars")
    val guestStars: List<GuestStar>?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("production_code")
    val productionCode: String?,
    @SerializedName("season_number")
    val seasonNumber: Int?,
    @SerializedName("still_path")
    val stillPath: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("vote_count")
    val voteCount: Int?
) {
    fun getAverage(): String {
        return voteAverage?.toBigDecimal()?.setScale(1, RoundingMode.UP).toString()
    }
}