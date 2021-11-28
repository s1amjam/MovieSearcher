package com.moviesearcher.tv.episode.model.image

import com.google.gson.annotations.SerializedName

data class Still(
    @SerializedName("aspect_ratio")
    val aspectRatio: Double?,
    @SerializedName("file_path")
    val filePath: String?,
    val height: Int?,
    @SerializedName("iso_639_1")
    val iso6391: Any?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("vote_count")
    val voteCount: Int?,
    val width: Int?
)