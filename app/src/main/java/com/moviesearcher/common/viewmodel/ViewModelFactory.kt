package com.moviesearcher.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moviesearcher.favorite.FavoriteViewModel
import com.moviesearcher.movie.MovieViewModel
import com.moviesearcher.tv.TvViewModel
import com.moviesearcher.tv.seasons.TvSeasonViewModel
import com.moviesearcher.watchlist.common.viewmodel.WatchlistViewModel

class ViewModelFactory(
    private val sessionId: String? = null,
    private val accountId: Long? = null,
    private val movieId: Long? = null,
    private val tvId: Long? = null,
    private val seasonNumber: String? = null,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WatchlistViewModel::class.java)) {
            return WatchlistViewModel(accountId!!, sessionId!!) as T
        }
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return MovieViewModel(movieId!!) as T
        }
        if (modelClass.isAssignableFrom(TvViewModel::class.java)) {
            return TvViewModel(tvId!!) as T
        }
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(sessionId!!, accountId!!) as T
        }
        if (modelClass.isAssignableFrom(TvSeasonViewModel::class.java)) {
            return TvSeasonViewModel(tvId, seasonNumber) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}