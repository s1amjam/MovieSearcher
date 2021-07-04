package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.watchlist.movie.MovieWatchlistResponse

class MovieWatchlistViewModel : ViewModel() {
    lateinit var movieWatchlistItemLiveData: LiveData<MovieWatchlistResponse>

    fun getMovieWatchlist(accountId: Int, sessionId: String) {
        movieWatchlistItemLiveData = Api.getMovieWatchlist(accountId, sessionId)
    }
}