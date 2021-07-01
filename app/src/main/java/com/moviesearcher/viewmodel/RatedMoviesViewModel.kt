package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.rated.movie.RatedMoviesResponse

class RatedMoviesViewModel : ViewModel() {
    lateinit var ratedMoviesItemLiveData: LiveData<RatedMoviesResponse>

    fun getRatedMovies(accountId: Int, sessionId: String) {
        ratedMoviesItemLiveData = Api.getRatedMovies(accountId, sessionId)
    }
}