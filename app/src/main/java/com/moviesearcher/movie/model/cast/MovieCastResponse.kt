package com.moviesearcher.movie.model.cast

data class MovieCastResponse(
    var cast: List<Cast>?,
    val crew: List<Crew>?,
    val id: Int?
)