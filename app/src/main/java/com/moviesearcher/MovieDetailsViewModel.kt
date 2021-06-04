package com.moviesearcher

import androidx.lifecycle.LiveData
import com.moviesearcher.Constants.ACCESS_TOKEN
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.moviedetails.MovieDetailsResponse

class MovieDetailsViewModel(movieId: Int) {
    val movieDetailsLiveData: LiveData<MovieDetailsResponse>

    init {
        movieDetailsLiveData = Api.getMovieDetails(
            ACCESS_TOKEN,
            movieId
        )
    }
}