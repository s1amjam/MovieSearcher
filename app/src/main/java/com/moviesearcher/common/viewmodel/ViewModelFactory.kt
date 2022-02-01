package com.moviesearcher.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moviesearcher.movie.MovieViewModel
import com.moviesearcher.watchlist.common.viewmodel.WatchlistViewModel

class ViewModelFactory(
    private val sessionId: String? = null,
    private val accountId: Long? = null,
    private val movieId: Long? = null
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WatchlistViewModel::class.java)) {
            return WatchlistViewModel(accountId!!, sessionId!!) as T
        }
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return MovieViewModel(movieId!!) as T
        }
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return MovieViewModel(movieId!!) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}