package com.moviesearcher.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.movie.model.MovieInfoResponse

class MovieInfoViewModel : ViewModel() {
    lateinit var movieInfo: LiveData<MovieInfoResponse>

    fun getMovieInfoById(movieId: Long): LiveData<MovieInfoResponse> {
        movieInfo = Api.getMovieInfo(movieId)

        return movieInfo
    }
}