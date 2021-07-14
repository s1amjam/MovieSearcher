package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.trending.TrendingResponse

class MovieViewModel : ViewModel() {
    val trendingMovies: LiveData<TrendingResponse>

    init {
        trendingMovies = Api.getTrending("movie", "week")
    }
}