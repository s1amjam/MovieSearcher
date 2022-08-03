package com.moviesearcher.tv.seasons

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.tv.seasons.model.TvSeasonResponse
import kotlinx.coroutines.launch

class TvSeasonViewModel(private val tvId: Long?, private val seasonNumber: String?) : ViewModel() {
    private val tvSeason = MutableLiveData<Resource<TvSeasonResponse>>()

    init {
        fetchTvSeason()
    }

    private fun fetchTvSeason() {
        viewModelScope.launch {
            tvSeason.postValue(Resource.loading(null))
            try {
                val tvSeasonFromApi = ApiService.create().getTvSeason(tvId, seasonNumber)
                tvSeason.postValue(Resource.success(tvSeasonFromApi))
            } catch (e: Exception) {
                tvSeason.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getTvSeason(): MutableLiveData<Resource<TvSeasonResponse>> {
        return tvSeason
    }
}