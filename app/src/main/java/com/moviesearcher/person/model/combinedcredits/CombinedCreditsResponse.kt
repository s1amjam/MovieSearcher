package com.moviesearcher.person.model.combinedcredits

data class CombinedCreditsResponse(
    var cast: List<Cast>?,
    var crew: List<Crew>?,
    val id: Int?
)