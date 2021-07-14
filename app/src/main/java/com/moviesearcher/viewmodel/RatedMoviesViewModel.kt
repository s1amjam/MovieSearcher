package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.rated.movie.RatedMoviesResponse

class RatedMoviesViewModel : ViewModel() {
    private lateinit var ratedMovies: LiveData<RatedMoviesResponse>

    fun getRatedMovies(accountId: Int, sessionId: String): LiveData<RatedMoviesResponse> {
        ratedMovies = Api.getRatedMovies(accountId, sessionId)

        return ratedMovies
    }
}