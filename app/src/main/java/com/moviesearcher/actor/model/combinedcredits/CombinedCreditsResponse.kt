package com.moviesearcher.actor.model.combinedcredits

data class CombinedCreditsResponse(
    val cast: List<Cast>?,
    val crew: List<Crew>?,
    val id: Int?
)