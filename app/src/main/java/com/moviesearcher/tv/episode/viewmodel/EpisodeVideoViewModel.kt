package com.moviesearcher.tv.episode.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.common.model.videos.VideosResponse

class EpisodeVideoViewModel : ViewModel() {
    private lateinit var episodeVideos: LiveData<VideosResponse>

    fun getTvEpisodeVideos(
        tvId: Long,
        seasonNumber: String,
        episodeNumber: Int
    ): LiveData<VideosResponse> {
        episodeVideos = Api.getTvEpisodeVideos(tvId, seasonNumber, episodeNumber)

        return episodeVideos
    }
}