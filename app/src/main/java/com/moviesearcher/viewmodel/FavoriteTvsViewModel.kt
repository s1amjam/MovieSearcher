package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.favorites.FavoriteTvResponse

class FavoriteTvsViewModel : ViewModel() {
    lateinit var favoriteTvsItemLiveData: LiveData<FavoriteTvResponse>

    fun getFavoriteTvs(accountId: Int, sessionId: String) {
        favoriteTvsItemLiveData = Api.getFavoriteTvs(accountId, sessionId)
    }
}