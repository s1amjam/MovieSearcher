package com.moviesearcher.common.model.images

data class ImagesResponse(
    val backdrops: List<Backdrop>?,
    val id: Int?,
    val posters: List<Poster>?
)