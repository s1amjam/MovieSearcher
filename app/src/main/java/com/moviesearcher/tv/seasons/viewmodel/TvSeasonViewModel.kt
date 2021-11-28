package com.moviesearcher.tv.seasons.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.tv.seasons.model.TvSeasonResponse

class TvSeasonViewModel : ViewModel() {
    private lateinit var tvSeason: LiveData<TvSeasonResponse>

    fun getTvSeason(
        tvId: Long?,
        seasonNumber: String?,
    ): LiveData<TvSeasonResponse> {
        tvSeason = Api.getTvSeason(tvId, seasonNumber)

        return tvSeason
    }
}