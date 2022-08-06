package com.moviesearcher.tv

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.credentials.CredentialsHolder
import com.moviesearcher.common.model.images.ImagesResponse
import com.moviesearcher.common.model.rate.AccountStatesResponse
import com.moviesearcher.common.model.videos.VideosResponse
import com.moviesearcher.common.utils.Constants.TV_ID
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.favorite.tv.model.FavoriteTvResponse
import com.moviesearcher.tv.model.TvInfoResponse
import com.moviesearcher.tv.model.cast.TvCastResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val credentialsHolder: CredentialsHolder
) : ViewModel() {
    private val info = MutableLiveData<Resource<TvInfoResponse>>()
    private val cast = MutableLiveData<Resource<TvCastResponse>>()
    private val recommendations = MutableLiveData<Resource<FavoriteTvResponse>>()
    private val videos = MutableLiveData<Resource<VideosResponse>>()
    private val images = MutableLiveData<Resource<ImagesResponse>>()
    private val accountStates = MutableLiveData<Resource<AccountStatesResponse>>()

    init {
        fetchTvInfo()
        fetchTvCast()
        fetchRecommendations()
        fetchVideos()
        fetchImages()
        fetchAccountStates()
    }

    private fun fetchTvInfo() {
        viewModelScope.launch {
            info.postValue(Resource.loading(null))
            try {
                val tvInfoFromApi = savedStateHandle.get<Long>(TV_ID)
                    ?.let { ApiService.create().tvInfo(it) }
                info.postValue(Resource.success(tvInfoFromApi))
            } catch (e: Exception) {
                info.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getTvInfo(): MutableLiveData<Resource<TvInfoResponse>> {
        return info
    }

    private fun fetchTvCast() {
        viewModelScope.launch {
            cast.postValue(Resource.loading(null))
            try {
                val tvCastFromApi = savedStateHandle.get<Long>(TV_ID)
                    ?.let { ApiService.create().tvCast(it) }
                cast.postValue(Resource.success(tvCastFromApi))
            } catch (e: Exception) {
                cast.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getTvCast(): MutableLiveData<Resource<TvCastResponse>> {
        return cast
    }

    private fun fetchRecommendations() {
        viewModelScope.launch {
            recommendations.postValue(Resource.loading(null))
            try {
                val recommendationsFromApi =
                    savedStateHandle.get<Long>(TV_ID)
                        ?.let { ApiService.create().tvRecommendations(it) }
                recommendations.postValue(Resource.success(recommendationsFromApi))
            } catch (e: Exception) {
                recommendations.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getRecommendations(): MutableLiveData<Resource<FavoriteTvResponse>> {
        return recommendations
    }

    private fun fetchVideos() {
        viewModelScope.launch {
            videos.postValue(Resource.loading(null))
            try {
                val videosFromApi = savedStateHandle.get<Long>(TV_ID)
                    ?.let { ApiService.create().tvVideos(it) }
                videos.postValue(Resource.success(videosFromApi))
            } catch (e: Exception) {
                videos.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getVideos(): MutableLiveData<Resource<VideosResponse>> {
        return videos
    }

    private fun fetchImages() {
        viewModelScope.launch {
            images.postValue(Resource.loading(null))
            try {
                val imagesFromApi = savedStateHandle.get<Long>(TV_ID)
                    ?.let { ApiService.create().tvImages(it) }
                images.postValue(Resource.success(imagesFromApi))
            } catch (e: Exception) {
                images.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getImages(): MutableLiveData<Resource<ImagesResponse>> {
        return images
    }

    private fun fetchAccountStates() {
        viewModelScope.launch {
            accountStates.postValue(Resource.loading(null))
            try {
                val accountStatesFromApi =
                    savedStateHandle.get<Long>(TV_ID)
                        ?.let {
                            ApiService.create()
                                .getTvAccountStates(it, credentialsHolder.getSessionId())
                        }
                accountStates.postValue(Resource.success(accountStatesFromApi))
            } catch (e: Exception) {
                accountStates.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getAccountStates(): MutableLiveData<Resource<AccountStatesResponse>> {
        return accountStates
    }
}