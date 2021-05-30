package com.moviesearcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.Constants.ACCESS_TOKEN
import com.moviesearcher.api.Api
import com.moviesearcher.entity.TrendingResponse

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