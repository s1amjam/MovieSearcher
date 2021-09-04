package com.moviesearcher.watchlist.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.watchlist.movie.model.MovieWatchlistResponse

class MovieWatchlistViewModel : ViewModel() {
    private lateinit var _movieWatchlist: MutableLiveData<MovieWatchlistResponse>
    val movieWatchlist: LiveData<MovieWatchlistResponse> get() = _movieWatchlist

    fun getMovieWatchlist(accountId: Long, sessionId: String): LiveData<MovieWatchlistResponse> {
        _movieWatchlist = Api.getMovieWatchlist(accountId, sessionId)

        return movieWatchlist
    }

    fun getMovieWatchlistIds(): MutableList<Long> {
        val movieWatchlistIds: MutableList<Long> = mutableListOf()
        _movieWatchlist.value?.results?.forEach { it -> movieWatchlistIds.add(it.id!!.toLong()) }

        return movieWatchlistIds
    }
}