package com.moviesearcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.Constants.ACCESS_TOKEN
import com.moviesearcher.api.Api
import com.moviesearcher.entity.TrendingResponse

class TvViewModel : ViewModel() {
    val tvItemLiveData: LiveData<TrendingResponse>

    init {
        tvItemLiveData = Api.getTrending(
            ACCESS_TOKEN,
            "tv",
            "week"
        )
    }
}