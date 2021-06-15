package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.trending.TrendingResponse

class TvViewModel : ViewModel() {
    val tvItemLiveData: LiveData<TrendingResponse>

    init {
        tvItemLiveData = Api.getTrending(
            "tv",
            "week"
        )
    }
}