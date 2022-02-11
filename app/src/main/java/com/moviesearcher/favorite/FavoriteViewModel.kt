package com.moviesearcher.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.favorite.movie.model.FavoriteMovieResponse
import com.moviesearcher.favorite.tv.model.FavoriteTvResponse
import kotlinx.coroutines.launch

class FavoriteViewModel(private val sessionId: String, private val accountId: Long) : ViewModel() {
    private val favoriteMovies = MutableLiveData<Resource<FavoriteMovieResponse>>()
    private val favoriteTvs = MutableLiveData<Resource<FavoriteTvResponse>>()

    init {
        fetchFavoriteMovie()
        fetchFavoriteTv()
    }

    private fun fetchFavoriteMovie() {
        viewModelScope.launch {
            favoriteMovies.postValue(Resource.loading(null))
            try {
                val favoriteMovieFromApi =
                    ApiService.create().getFavoriteMovies(accountId, sessionId)
                favoriteMovies.postValue(Resource.success(favoriteMovieFromApi))
            } catch (e: Exception) {
                favoriteMovies.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getFavoriteMovie(): MutableLiveData<Resource<FavoriteMovieResponse>> {
        return favoriteMovies
    }

    private fun fetchFavoriteTv() {
        viewModelScope.launch {
            favoriteTvs.postValue(Resource.loading(null))
            try {
                val favoriteTvFromApi = ApiService.create().getFavoriteTvs(accountId, sessionId)
                favoriteTvs.postValue(Resource.success(favoriteTvFromApi))
            } catch (e: Exception) {
                favoriteTvs.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getFavoriteTv(): MutableLiveData<Resource<FavoriteTvResponse>> {
        return favoriteTvs
    }
}