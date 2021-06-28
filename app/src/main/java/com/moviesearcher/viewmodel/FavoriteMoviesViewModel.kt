package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.favorites.FavoriteMovieResponse

class FavoriteMoviesViewModel : ViewModel() {
    lateinit var favoriteMoviesItemLiveData: LiveData<FavoriteMovieResponse>

    fun getFavoriteMovies(accountId: Int, sessionId: String) {
        favoriteMoviesItemLiveData = Api.getFavoriteMovies(accountId, sessionId)
    }
}