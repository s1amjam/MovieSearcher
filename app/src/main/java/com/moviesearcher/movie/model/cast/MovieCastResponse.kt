package com.moviesearcher.movie.model.cast

data class MovieCastResponse(
    val cast: List<Cast>?,
    val crew: List<Crew>?,
    val id: Int?
)