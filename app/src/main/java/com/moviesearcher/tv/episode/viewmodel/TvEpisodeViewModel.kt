package com.moviesearcher.tv.episode.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.tv.episode.model.TvEpisodeResponse

class TvEpisodeViewModel : ViewModel() {
    private lateinit var tvEpisode: LiveData<TvEpisodeResponse>

    fun getTvEpisode(
        tvId: Long,
        seasonNumber: String,
        episodeNumber: Int
    ): LiveData<TvEpisodeResponse> {
        tvEpisode = Api.getTvEpisode(tvId, seasonNumber, episodeNumber)

        return tvEpisode
    }
}