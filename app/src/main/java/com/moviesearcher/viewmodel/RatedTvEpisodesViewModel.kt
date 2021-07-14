package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.rated.tvepisode.RatedTvEpisodesResponse

class RatedTvEpisodesViewModel : ViewModel() {
    private lateinit var ratedTvEpisodes: LiveData<RatedTvEpisodesResponse>

    fun getRatedTvEpisodes(accountId: Int, sessionId: String): LiveData<RatedTvEpisodesResponse> {
        ratedTvEpisodes = Api.getRatedTvEpisodes(accountId, sessionId)

        return ratedTvEpisodes
    }
}