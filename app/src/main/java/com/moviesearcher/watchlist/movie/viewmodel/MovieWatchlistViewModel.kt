package com.moviesearcher.watchlist.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.watchlist.movie.model.MovieWatchlistResponse

class MovieWatchlistViewModel : ViewModel() {
    lateinit var movieWatchlist: LiveData<MovieWatchlistResponse>

    fun getMovieWatchlist(accountId: Int, sessionId: String): LiveData<MovieWatchlistResponse> {
        movieWatchlist = Api.getMovieWatchlist(accountId, sessionId)

        return movieWatchlist
    }
}