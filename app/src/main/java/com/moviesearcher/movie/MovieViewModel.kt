package com.moviesearcher.movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.credentials.CredentialsHolder
import com.moviesearcher.common.model.images.ImagesResponse
import com.moviesearcher.common.model.rate.AccountStatesResponse
import com.moviesearcher.common.model.videos.VideosResponse
import com.moviesearcher.common.utils.Constants.MOVIE_ID
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.favorite.movie.model.FavoriteMovieResponse
import com.moviesearcher.movie.model.MovieInfoResponse
import com.moviesearcher.movie.model.cast.MovieCastResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val credentialsHolder: CredentialsHolder
) : ViewModel() {
    private val info = MutableLiveData<Resource<MovieInfoResponse>>()
    private val cast = MutableLiveData<Resource<MovieCastResponse>>()
    private val recommendations = MutableLiveData<Resource<FavoriteMovieResponse>>()
    private val videos = MutableLiveData<Resource<VideosResponse>>()
    private val images = MutableLiveData<Resource<ImagesResponse>>()
    private val accountStates = MutableLiveData<Resource<AccountStatesResponse>>()

    init {
        fetchMovieInfo()
        fetchMovieCast()
        fetchRecommendations()
        fetchVideos()
        fetchImages()
        fetchAccountStates()
    }

    private fun fetchMovieInfo() {
        viewModelScope.launch {
            info.postValue(Resource.loading(null))
            try {
                val movieInfoFromApi = savedStateHandle.get<Long>(MOVIE_ID)
                    ?.let { ApiService.create().movieInfo(it) }
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
                val movieCastFromApi = savedStateHandle.get<Long>(MOVIE_ID)
                    ?.let { ApiService.create().movieCast(it) }
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
                val recommendationsFromApi =
                    savedStateHandle.get<Long>(MOVIE_ID)
                        ?.let { ApiService.create().recommendations(it) }
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
                val videosFromApi = savedStateHandle.get<Long>(MOVIE_ID)
                    ?.let { ApiService.create().videos(it) }
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
                val imagesFromApi = savedStateHandle.get<Long>(MOVIE_ID)
                    ?.let { ApiService.create().images(it) }
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
                    savedStateHandle.get<Long>(MOVIE_ID)
                        ?.let {
                            ApiService.create()
                                .getMovieAccountStates(it, credentialsHolder.getSessionId())
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