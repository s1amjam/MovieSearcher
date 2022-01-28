package com.moviesearcher.movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.model.images.ImagesResponse
import com.moviesearcher.common.model.videos.VideosResponse
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.favorite.movie.model.FavoriteMovieResponse
import com.moviesearcher.movie.model.MovieInfoResponse
import com.moviesearcher.movie.model.cast.MovieCastResponse
import kotlinx.coroutines.launch

class MovieViewModel(private val movieId: Long) : ViewModel() {
    private val info = MutableLiveData<Resource<MovieInfoResponse>>()
    private val cast = MutableLiveData<Resource<MovieCastResponse>>()
    private val recommendations = MutableLiveData<Resource<FavoriteMovieResponse>>()
    private val videos = MutableLiveData<Resource<VideosResponse>>()
    private val images = MutableLiveData<Resource<ImagesResponse>>()

    init {
        fetchMovieInfo()
        fetchMovieCast()
        fetchRecommendations()
        fetchVideos()
        fetchImages()
    }

    private fun fetchMovieInfo() {
        viewModelScope.launch {
            info.postValue(Resource.loading(null))
            try {
                val movieInfoFromApi = ApiService.create().movieInfo(movieId)
                info.postValue(Resource.success(movieInfoFromApi))
            } catch (e: Exception) {
                info.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getMovieInfo(): MutableLiveData<Resource<MovieInfoResponse>> {
        return info
    }

    private fun fetchMovieCast() {
        viewModelScope.launch {
            cast.postValue(Resource.loading(null))
            try {
                val movieCastFromApi = ApiService.create().movieCast(movieId)
                cast.postValue(Resource.success(movieCastFromApi))
            } catch (e: Exception) {
                cast.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getMovieCast(): MutableLiveData<Resource<MovieCastResponse>> {
        return cast
    }

    private fun fetchRecommendations() {
        viewModelScope.launch {
            recommendations.postValue(Resource.loading(null))
            try {
                val recommendationsFromApi = ApiService.create().recommendations(movieId)
                recommendations.postValue(Resource.success(recommendationsFromApi))
            } catch (e: Exception) {
                recommendations.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getRecommendations(): MutableLiveData<Resource<FavoriteMovieResponse>> {
        return recommendations
    }

    private fun fetchVideos() {
        viewModelScope.launch {
            videos.postValue(Resource.loading(null))
            try {
                val videosFromApi = ApiService.create().videos(movieId)
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
                val imagesFromApi = ApiService.create().images(movieId)
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