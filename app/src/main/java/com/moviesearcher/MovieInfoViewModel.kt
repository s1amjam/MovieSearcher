package com.moviesearcher

import androidx.lifecycle.LiveData
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.moviedetails.MovieInfoResponse
import com.moviesearcher.api.entity.utils.Constants.ACCESS_TOKEN

class MovieInfoViewModel(movieId: Int) {
    val movieInfoLiveData: LiveData<MovieInfoResponse>

    init {
        movieInfoLiveData = Api.getMovieDetails(
            ACCESS_TOKEN,
            movieId
        )
    }
}