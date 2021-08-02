package com.moviesearcher.favorite.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.favorite.movie.model.FavoriteMovieResponse

class FavoriteMoviesViewModel : ViewModel() {
    private lateinit var favoriteMovies: LiveData<FavoriteMovieResponse>

    fun getFavoriteMovies(accountId: Long, sessionId: String): LiveData<FavoriteMovieResponse> {
        favoriteMovies = Api.getFavoriteMovies(accountId, sessionId)

        return favoriteMovies
    }
}