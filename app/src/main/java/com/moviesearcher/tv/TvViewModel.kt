package com.moviesearcher.tv

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.model.images.ImagesResponse
import com.moviesearcher.common.model.videos.VideosResponse
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.favorite.tv.model.FavoriteTvResponse
import com.moviesearcher.tv.model.TvInfoResponse
import com.moviesearcher.tv.model.cast.TvCastResponse
import kotlinx.coroutines.launch

class TvViewModel(private val tvId: Long) : ViewModel() {
    private val info = MutableLiveData<Resource<TvInfoResponse>>()
    private val cast = MutableLiveData<Resource<TvCastResponse>>()
    private val recommendations = MutableLiveData<Resource<FavoriteTvResponse>>()
    private val videos = MutableLiveData<Resource<VideosResponse>>()
    private val images = MutableLiveData<Resource<ImagesResponse>>()

    init {
        fetchTvInfo()
        fetchTvCast()
        fetchRecommendations()
        fetchVideos()
        fetchImages()
    }

    private fun fetchTvInfo() {
        viewModelScope.launch {
            info.postValue(Resource.loading(null))
            try {
                val tvInfoFromApi = ApiService.create().tvInfo(tvId)
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
                val tvCastFromApi = ApiService.create().tvCast(tvId)
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
                val recommendationsFromApi = ApiService.create().tvRecommendations(tvId)
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
                val videosFromApi = ApiService.create().videos(tvId)
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
                val imagesFromApi = ApiService.create().tvImages(tvId)
                images.postValue(Resource.success(imagesFromApi))
            } catch (e: Exception) {
                images.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getImages(): MutableLiveData<Resource<ImagesResponse>> {
        return images
    }
}