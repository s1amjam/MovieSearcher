package com.moviesearcher.tv.model.cast

data class TvCastResponse(
    val cast: List<Cast>?,
    val crew: List<Crew>?,
    val id: Int?
)