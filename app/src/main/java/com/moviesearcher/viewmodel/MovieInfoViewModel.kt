package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.movieinfo.MovieInfoResponse
import com.moviesearcher.utils.Constants.ACCESS_TOKEN

class MovieInfoViewModel(movieId: Int) {
    val movieInfoLiveData: LiveData<MovieInfoResponse>

    init {
        movieInfoLiveData = Api.getMovieInfo(
            ACCESS_TOKEN,
            movieId
        )
    }
}