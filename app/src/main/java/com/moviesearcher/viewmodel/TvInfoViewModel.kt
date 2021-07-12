package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.tvinfo.TvInfoResponse

class TvInfoViewModel(tvId: Long) {
    val tvInfoLiveData: LiveData<TvInfoResponse>

    init {
        tvInfoLiveData = Api.getTvInfo(tvId)
    }
}