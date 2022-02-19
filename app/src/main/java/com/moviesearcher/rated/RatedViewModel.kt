package com.moviesearcher.rated

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.rated.movie.model.RatedMoviesResponse
import com.moviesearcher.rated.tv.model.RatedTvsResponse
import com.moviesearcher.rated.tvepisode.model.RatedTvEpisodesResponse
import kotlinx.coroutines.launch

class RatedViewModel(private val sessionId: String, private val accountId: Long) : ViewModel() {
    private val ratedMovies = MutableLiveData<Resource<RatedMoviesResponse>>()
    private val ratedTvs = MutableLiveData<Resource<RatedTvsResponse>>()
    private val ratedTvEpisodes = MutableLiveData<Resource<RatedTvEpisodesResponse>>()

    init {
        fetchRatedMovies()
        fetchRatedTvs()
        fetchRatedTvEpisodes()
    }

    private fun fetchRatedMovies() {
        viewModelScope.launch {
            ratedMovies.postValue(Resource.loading(null))
            try {
                val favoriteMovieFromApi =
                    ApiService.create().getRatedMovies(accountId, sessionId)
                ratedMovies.postValue(Resource.success(favoriteMovieFromApi))
            } catch (e: Exception) {
                ratedMovies.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getRatedMovies(): MutableLiveData<Resource<RatedMoviesResponse>> {
        return ratedMovies
    }

    private fun fetchRatedTvs() {
        viewModelScope.launch {
            ratedTvs.postValue(Resource.loading(null))
            try {
                val favoriteTvFromApi = ApiService.create().getRatedTvs(accountId, sessionId)
                ratedTvs.postValue(Resource.success(favoriteTvFromApi))
            } catch (e: Exception) {
                ratedTvs.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getRatedTvs(): MutableLiveData<Resource<RatedTvsResponse>> {
        return ratedTvs
    }

    private fun fetchRatedTvEpisodes() {
        viewModelScope.launch {
            ratedTvEpisodes.postValue(Resource.loading(null))
            try {
                val favoriteTvFromApi = ApiService.create().getRatedTvEpisodes(accountId, sessionId)
                ratedTvEpisodes.postValue(Resource.success(favoriteTvFromApi))
            } catch (e: Exception) {
                ratedTvEpisodes.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getRatedTvEpisodes(): MutableLiveData<Resource<RatedTvEpisodesResponse>> {
        return ratedTvEpisodes
    }
}