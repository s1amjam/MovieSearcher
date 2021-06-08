package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.tvinfo.TvInfoResponse
import com.moviesearcher.utils.Constants.ACCESS_TOKEN

class TvInfoViewModel(tvId: Int) {
    val tvInfoLiveData: LiveData<TvInfoResponse>

    init {
        tvInfoLiveData = Api.getTvInfo(
            ACCESS_TOKEN,
            tvId
        )
    }
}