package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.favorites.FavoriteTvResponse

class FavoriteTvsViewModel : ViewModel() {
    lateinit var favoriteTvs: LiveData<FavoriteTvResponse>

    fun getFavoriteTvs(accountId: Int, sessionId: String): LiveData<FavoriteTvResponse> {
        favoriteTvs = Api.getFavoriteTvs(accountId, sessionId)

        return favoriteTvs
    }
}