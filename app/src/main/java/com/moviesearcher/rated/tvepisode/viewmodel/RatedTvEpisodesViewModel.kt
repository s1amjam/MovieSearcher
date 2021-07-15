package com.moviesearcher.rated.tvepisode.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.rated.tvepisode.model.RatedTvEpisodesResponse

class RatedTvEpisodesViewModel : ViewModel() {
    private lateinit var ratedTvEpisodes: LiveData<RatedTvEpisodesResponse>

    fun getRatedTvEpisodes(accountId: Int, sessionId: String): LiveData<RatedTvEpisodesResponse> {
        ratedTvEpisodes = Api.getRatedTvEpisodes(accountId, sessionId)

        return ratedTvEpisodes
    }
}