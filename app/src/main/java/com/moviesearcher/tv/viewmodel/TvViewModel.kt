package com.moviesearcher.tv.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.movie.model.TrendingResponse

class TvViewModel : ViewModel() {
    val trendingTvs: LiveData<TrendingResponse>

    init {
        trendingTvs = Api.getTrending(
            "tv",
            "week"
        )
    }
}