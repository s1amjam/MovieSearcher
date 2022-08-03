package com.moviesearcher.tv.episode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.model.videos.VideosResponse
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.tv.episode.model.TvEpisodeResponse
import com.moviesearcher.tv.episode.model.image.EpisodeImageResponse
import kotlinx.coroutines.launch

class TvEpisodeViewModel(
    private val tvId: Long,
    private val seasonNumber: String,
    private val episodeNumber: Int,
) : ViewModel() {
    private val tvEpisodeImages = MutableLiveData<Resource<EpisodeImageResponse>>()
    private val episodeVideos = MutableLiveData<Resource<VideosResponse>>()
    private val tvEpisode = MutableLiveData<Resource<TvEpisodeResponse>>()

    init {
        fetchTvEpisode()
        fetchTvEpisodeImages()
        fetchTvEpisodeVideos()
    }

    private fun fetchTvEpisode() {
        viewModelScope.launch {
            tvEpisode.postValue(Resource.loading(null))
            try {
                val tvEpisodeFromApi =
                    ApiService.create().getTvEpisode(tvId, seasonNumber, episodeNumber)
                tvEpisode.postValue(Resource.success(tvEpisodeFromApi))
            } catch (e: Exception) {
                tvEpisode.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getTvEpisode(): MutableLiveData<Resource<TvEpisodeResponse>> {
        return tvEpisode
    }

    private fun fetchTvEpisodeImages() {
        viewModelScope.launch {
            tvEpisodeImages.postValue(Resource.loading(null))
            try {
                val tvEpisodeImagesFromApi =
                    ApiService.create().getTvEpisodeImages(tvId, seasonNumber, episodeNumber)
                tvEpisodeImages.postValue(Resource.success(tvEpisodeImagesFromApi))
            } catch (e: Exception) {
                tvEpisodeImages.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getTvEpisodeImages(): MutableLiveData<Resource<EpisodeImageResponse>> {
        return tvEpisodeImages
    }

    private fun fetchTvEpisodeVideos() {
        viewModelScope.launch {
            episodeVideos.postValue(Resource.loading(null))
            try {
                val tvEpisodeVideosFromApi =
                    ApiService.create().getTvEpisodeVideos(tvId, seasonNumber, episodeNumber)
                episodeVideos.postValue(Resource.success(tvEpisodeVideosFromApi))
            } catch (e: Exception) {
                episodeVideos.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getTvEpisodeVideos(): MutableLiveData<Resource<VideosResponse>> {
        return episodeVideos
    }
}