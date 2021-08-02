package com.moviesearcher.favorite.tv.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.favorite.tv.model.FavoriteTvResponse

class FavoriteTvsViewModel : ViewModel() {
    lateinit var favoriteTvs: LiveData<FavoriteTvResponse>

    fun getFavoriteTvs(accountId: Long, sessionId: String): LiveData<FavoriteTvResponse> {
        favoriteTvs = Api.getFavoriteTvs(accountId, sessionId)

        return favoriteTvs
    }
}