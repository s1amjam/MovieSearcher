package com.moviesearcher.tv.episode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.model.videos.VideosResponse
import com.moviesearcher.common.utils.Constants.EPISODE_NUMBER
import com.moviesearcher.common.utils.Constants.SEASON_NUMBER
import com.moviesearcher.common.utils.Constants.TV_ID
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.tv.episode.model.TvEpisodeResponse
import com.moviesearcher.tv.episode.model.image.EpisodeImageResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvEpisodeViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) :
    ViewModel() {

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
                    savedStateHandle.get<Long>(TV_ID)?.let {
                        savedStateHandle.get<String>(SEASON_NUMBER)
                            ?.let { it1 ->
                                savedStateHandle.get<Int>(EPISODE_NUMBER)?.let { it2 ->
                                    ApiService.create().getTvEpisode(
                                        it,
                                        it1,
                                        it2
                                    )
                                }
                            }
                    }
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
                    savedStateHandle.get<Long>(TV_ID)?.let {
                        savedStateHandle.get<String>(SEASON_NUMBER)
                            ?.let { it1 ->
                                savedStateHandle.get<Int>(EPISODE_NUMBER)?.let { it2 ->
                                    ApiService.create().getTvEpisodeImages(
                                        it,
                                        it1,
                                        it2
                                    )
                                }
                            }
                    }
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
                    savedStateHandle.get<Long>(TV_ID)?.let {
                        savedStateHandle.get<String>(SEASON_NUMBER)
                            ?.let { it1 ->
                                savedStateHandle.get<Int>(EPISODE_NUMBER)?.let { it2 ->
                                    ApiService.create().getTvEpisodeVideos(
                                        it,
                                        it1,
                                        it2
                                    )
                                }
                            }
                    }
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