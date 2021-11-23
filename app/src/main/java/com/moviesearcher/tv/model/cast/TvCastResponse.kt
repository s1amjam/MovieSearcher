package com.moviesearcher.tv.model.cast

data class TvCastResponse(
    var cast: List<Cast>?,
    val crew: List<Crew>?,
    val id: Int?
)