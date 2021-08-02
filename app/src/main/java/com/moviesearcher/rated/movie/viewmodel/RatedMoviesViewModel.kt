package com.moviesearcher.rated.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.rated.movie.model.RatedMoviesResponse

class RatedMoviesViewModel : ViewModel() {
    private lateinit var ratedMovies: LiveData<RatedMoviesResponse>

    fun getRatedMovies(accountId: Long, sessionId: String): LiveData<RatedMoviesResponse> {
        ratedMovies = Api.getRatedMovies(accountId, sessionId)

        return ratedMovies
    }
}