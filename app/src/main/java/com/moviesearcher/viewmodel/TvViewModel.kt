package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.trending.TrendingResponse

class TvViewModel : ViewModel() {
    val tvs: LiveData<TrendingResponse>

    init {
        tvs = Api.getTrending(
            "tv",
            "week"
        )
    }
}