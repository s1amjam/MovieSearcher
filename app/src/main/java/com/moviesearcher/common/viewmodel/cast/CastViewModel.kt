package com.moviesearcher.common.viewmodel.cast

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.movie.model.cast.MovieCastResponse
import com.moviesearcher.tv.model.cast.TvCastResponse

class CastViewModel : ViewModel() {
    lateinit var movieCast: LiveData<MovieCastResponse>
    lateinit var tvCast: LiveData<TvCastResponse>

    fun getTvCastById(tvId: Long): LiveData<TvCastResponse> {
        tvCast = Api.getTvCast(tvId)

        return tvCast
    }

    fun getMovieCastById(movieId: Long): LiveData<MovieCastResponse> {
        movieCast = Api.getMovieCast(movieId)

        return movieCast
    }
}