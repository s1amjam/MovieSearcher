package com.moviesearcher.rated.tv.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.rated.tv.model.RatedTvsResponse

class RatedTvsViewModel : ViewModel() {
    private lateinit var ratedTvs: LiveData<RatedTvsResponse>

    fun getRatedTvs(accountId: Int, sessionId: String): LiveData<RatedTvsResponse> {
        ratedTvs = Api.getRatedTvs(accountId, sessionId)

        return ratedTvs
    }
}