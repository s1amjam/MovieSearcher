package com.moviesearcher.favorite

import android.content.Context
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.R
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.model.common.ResponseWithCodeAndMessage
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.common.utils.Status
import com.moviesearcher.favorite.model.MarkAsFavoriteRequest
import com.moviesearcher.favorite.movie.model.FavoriteMovieResponse
import com.moviesearcher.favorite.tv.model.FavoriteTvResponse
import com.moviesearcher.list.ERROR_MESSAGE
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val sessionId: String,
    private val accountId: Long,
    isFavorite: Boolean
) : ViewModel() {
    private val favoriteMovies = MutableLiveData<Resource<FavoriteMovieResponse>>()
    private val favoriteTvs = MutableLiveData<Resource<FavoriteTvResponse>>()
    private val markAsFavorite = MutableLiveData<Resource<ResponseWithCodeAndMessage>>()

    private val markAsFavoriteIcon = R.drawable.ic_round_star_outline_36
    private val removeFromFavoriteIcon = R.drawable.ic_round_star_filled_36

    private var _isFavorite = isFavorite

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
        if (favoriteMovies.value == null) {
            fetchFavoriteMovie()
        }

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
        if (favoriteTvs.value == null) {
            fetchFavoriteTv()
        }

        return favoriteTvs
    }

    fun checkFavorites(
        button: ImageButton,
        viewLifecycleOwner: LifecycleOwner,
        mediaInfo: MutableMap<String, Long>,
        context: Context
    ) {
        val mediaId = mediaInfo.values.first()
        val mediaKey = mediaInfo.keys.first()

        if (mediaKey == "movie") {
            getFavoriteMovie().observe(viewLifecycleOwner) { it ->
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { favoriteMovieItems ->
                            val ids = mutableListOf<Long>()
                            button.setImageResource(markAsFavoriteIcon)
                            favoriteMovieItems.results?.forEach { it.id?.let { id -> ids.add(id) } }

                            if (ids.contains(mediaId)) {
                                button.tag = "false"
                                button.setImageResource(removeFromFavoriteIcon)
                            } else {
                                button.tag = "true"
                            }
                        }
                    }
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                        Toast.makeText(
                            context,
                            ERROR_MESSAGE.format(it.message),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        } else {
            getFavoriteTv().observe(viewLifecycleOwner) { it ->
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { favoriteMovieItems ->
                            val ids = mutableListOf<Long>()
                            button.setImageResource(markAsFavoriteIcon)
                            favoriteMovieItems.results?.forEach { it.id?.let { id -> ids.add(id) } }

                            if (ids.contains(mediaId)) {
                                button.tag = "false"
                                button.setImageResource(removeFromFavoriteIcon)
                            } else {
                                button.tag = "true"
                            }
                        }
                    }
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                        Toast.makeText(
                            context,
                            ERROR_MESSAGE.format(it.message),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun fetchMarkAsFavorite(
        accountId: Long,
        sessionId: String,
        markAsFavoriteRequest: MarkAsFavoriteRequest
    ) {
        viewModelScope.launch {
            markAsFavorite.postValue(Resource.loading(null))
            try {
                val markAsFavoriteFromApi =
                    ApiService.create().markAsFavorite(accountId, sessionId, markAsFavoriteRequest)
                markAsFavorite.postValue(Resource.success(markAsFavoriteFromApi))
            } catch (e: Exception) {
                markAsFavorite.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun postMarkAsFavorite(
        accountId: Long,
        sessionId: String,
        markAsFavoriteRequest: MarkAsFavoriteRequest
    ): MutableLiveData<Resource<ResponseWithCodeAndMessage>> {
        fetchMarkAsFavorite(accountId, sessionId, markAsFavoriteRequest)

        return markAsFavorite
    }

    fun processFavoriteButtons(button: ImageButton) {
        if (button.tag.toString().toBoolean()) {
            button.tag = "false"
            button.setImageResource(removeFromFavoriteIcon)
        } else {
            button.tag = "true"
            button.setImageResource(markAsFavoriteIcon)
        }
    }
}