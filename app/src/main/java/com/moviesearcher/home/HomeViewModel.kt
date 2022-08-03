package com.moviesearcher.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.movie.model.TrendingResponse
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val trendingMovies = MutableLiveData<Resource<TrendingResponse>>()
    private val trendingTvs = MutableLiveData<Resource<TrendingResponse>>()
    private val upcomingMovies = MutableLiveData<Resource<TrendingResponse>>()

    init {
        fetchTrendingMovies()
        fetchTrendingTvs()
        fetchUpcomingMovies()
    }

    private fun fetchTrendingMovies() {
        viewModelScope.launch {
            trendingMovies.postValue(Resource.loading(null))
            try {
                val trendingMoviesFromApi = ApiService.create().getTrending("movie", "day")
                trendingMovies.postValue(Resource.success(trendingMoviesFromApi))
            } catch (e: Exception) {
                trendingMovies.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getTrendingMovies(): LiveData<Resource<TrendingResponse>> {
        return trendingMovies
    }

    private fun fetchUpcomingMovies() {
        viewModelScope.launch {
            upcomingMovies.postValue(Resource.loading(null))
            try {
                val upcomingMoviesFromApi = ApiService.create().getUpcomingMovies()
                upcomingMovies.postValue(Resource.success(upcomingMoviesFromApi))
            } catch (e: Exception) {
                upcomingMovies.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getUpcomingMovies(): LiveData<Resource<TrendingResponse>> {
        return upcomingMovies
    }

    private fun fetchTrendingTvs() {
        viewModelScope.launch {
            trendingTvs.postValue(Resource.loading(null))
            try {
                val trendingTvsFromApi = ApiService.create().getTrending("tv", "day")
                trendingTvs.postValue(Resource.success(trendingTvsFromApi))
            } catch (e: Exception) {
                trendingTvs.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getTrendingTvs(): LiveData<Resource<TrendingResponse>> {
        return trendingTvs
    }
}