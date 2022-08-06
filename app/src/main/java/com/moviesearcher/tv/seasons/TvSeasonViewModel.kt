package com.moviesearcher.tv.seasons

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.utils.Constants.SEASON_NUMBER
import com.moviesearcher.common.utils.Constants.TV_ID
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.tv.seasons.model.TvSeasonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvSeasonViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) :
    ViewModel() {
    private val tvSeason = MutableLiveData<Resource<TvSeasonResponse>>()

    init {
        fetchTvSeason()
    }

    private fun fetchTvSeason() {
        viewModelScope.launch {
            tvSeason.postValue(Resource.loading(null))
            try {
                val tvSeasonFromApi = ApiService.create().getTvSeason(
                    savedStateHandle[TV_ID],
                    savedStateHandle[SEASON_NUMBER]
                )
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