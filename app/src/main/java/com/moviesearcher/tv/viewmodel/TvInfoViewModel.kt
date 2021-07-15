package com.moviesearcher.tv.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.tv.model.TvInfoResponse

class TvInfoViewModel : ViewModel() {
    private lateinit var tvInfo: LiveData<TvInfoResponse>

    fun getTvInfo(tvId: Long): LiveData<TvInfoResponse> {
        tvInfo = Api.getTvInfo(tvId)

        return tvInfo
    }
}