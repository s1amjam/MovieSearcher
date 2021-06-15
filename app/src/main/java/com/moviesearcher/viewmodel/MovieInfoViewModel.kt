package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.movieinfo.MovieInfoResponse

class MovieInfoViewModel : ViewModel() {
    lateinit var movieInfoLiveData: LiveData<MovieInfoResponse>

    fun getMovieInfoById(movieId: Int) {
        val movieInfoResponse = Api.getMovieInfo(movieId)
        this.movieInfoLiveData = movieInfoResponse
    }
}