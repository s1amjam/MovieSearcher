package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.rated.tv.RatedTvsResponse

class RatedTvsViewModel : ViewModel() {
    lateinit var ratedTvsItemLiveData: LiveData<RatedTvsResponse>

    fun getRatedTvs(accountId: Int, sessionId: String) {
        ratedTvsItemLiveData = Api.getRatedTvs(accountId, sessionId)
    }
}