package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.movieinfo.MovieInfoResponse
import com.moviesearcher.utils.Constants.ACCESS_TOKEN

class MovieInfoViewModel : ViewModel() {
    lateinit var movieInfoLiveData: LiveData<MovieInfoResponse>

    fun getMovieInfoById(movieId: Int) {
        val movieInfoResponse = Api.getMovieInfo(
            ACCESS_TOKEN,
            movieId
        )
        this.movieInfoLiveData = movieInfoResponse
    }
}