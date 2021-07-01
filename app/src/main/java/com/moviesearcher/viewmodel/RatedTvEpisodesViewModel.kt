package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.rated.tvepisode.RatedTvEpisodesResponse

class RatedTvEpisodesViewModel : ViewModel() {
    lateinit var ratedTvEpisodesItemLiveData: LiveData<RatedTvEpisodesResponse>

    fun getRatedTvEpisodes(accountId: Int, sessionId: String) {
        ratedTvEpisodesItemLiveData = Api.getRatedTvEpisodes(accountId, sessionId)
    }
}