package com.moviesearcher.common.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.model.common.ResponseWithCodeAndMessage
import com.moviesearcher.common.model.rate.Rated
import com.moviesearcher.common.utils.Resource
import kotlinx.coroutines.launch

class RateViewModel : ViewModel() {
    private val movieRate = MutableLiveData<Resource<ResponseWithCodeAndMessage>>()
    private val tvRate = MutableLiveData<Resource<ResponseWithCodeAndMessage>>()

    private fun fetchMovieRate(
        id: Long,
        sessionId: String,
        rate: Rated
    ) {
        viewModelScope.launch {
            movieRate.postValue(Resource.loading(null))
            try {
                val movieRateFromApi = ApiService.create().postMovieRating(id, sessionId, rate)
                movieRate.postValue(Resource.success(movieRateFromApi))
            } catch (e: Exception) {
                movieRate.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun postMovieRate(
        id: Long,
        sessionId: String,
        rate: Rated
    ): MutableLiveData<Resource<ResponseWithCodeAndMessage>> {
        fetchMovieRate(id, sessionId, rate)

        return movieRate
    }

    private fun fetchTvRate(
        id: Long,
        sessionId: String,
        rate: Rated
    ) {
        viewModelScope.launch {
            tvRate.postValue(Resource.loading(null))
            try {
                val tvRateFromApi = ApiService.create().postTvRating(id, sessionId, rate)
                tvRate.postValue(Resource.success(tvRateFromApi))
            } catch (e: Exception) {
                tvRate.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun postTvRate(
        id: Long,
        sessionId: String,
        rate: Rated
    ): MutableLiveData<Resource<ResponseWithCodeAndMessage>> {
        fetchTvRate(id, sessionId, rate)

        return tvRate
    }
}