package com.moviesearcher.tv.episode.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.tv.episode.model.image.EpisodeImageResponse

class EpisodeImagesViewModel : ViewModel() {
    private lateinit var tvEpisodeImages: LiveData<EpisodeImageResponse>

    fun getTvEpisodeImages(
        tvId: Long,
        seasonNumber: String,
        episodeNumber: Int
    ): LiveData<EpisodeImageResponse> {
        tvEpisodeImages = Api.getTvEpisodeImages(tvId, seasonNumber, episodeNumber)

        return tvEpisodeImages
    }
}