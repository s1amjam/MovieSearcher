package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.trending.TrendingResponse
import com.moviesearcher.utils.Constants.ACCESS_TOKEN

class MovieViewModel : ViewModel() {
    val movieItemLiveData: LiveData<TrendingResponse>

    init {
        movieItemLiveData = Api.getTrending(
            ACCESS_TOKEN,
            "movie",
            "week"
        )
    }
}