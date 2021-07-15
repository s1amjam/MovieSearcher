package com.moviesearcher.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.movie.model.TrendingResponse

class MovieViewModel : ViewModel() {
    val trendingMovies: LiveData<TrendingResponse>

    init {
        trendingMovies = Api.getTrending("movie", "week")
    }
}