package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.tvinfo.TvInfoResponse

class TvInfoViewModel : ViewModel() {
    private lateinit var tvInfo: LiveData<TvInfoResponse>

    fun getTvInfo(tvId: Long): LiveData<TvInfoResponse> {
        tvInfo = Api.getTvInfo(tvId)

        return tvInfo
    }
}