package com.moviesearcher.common.model.images

import com.google.gson.annotations.SerializedName

data class ImagesResponse(
    @SerializedName("backdrops")
    var backdrops: MutableList<Backdrop>?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("posters")
    var posters: MutableList<Poster>?
)