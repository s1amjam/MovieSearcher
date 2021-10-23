package com.moviesearcher.movie.viewmodel.cast

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.movie.model.cast.MovieCastResponse

class MovieCastViewModel : ViewModel() {
    lateinit var movieCast: LiveData<MovieCastResponse>

    fun getMovieCastById(movieId: Long): LiveData<MovieCastResponse> {
        movieCast = Api.getMovieCast(movieId)

        return movieCast
    }
}