package com.moviesearcher.watchlist.tv.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.watchlist.tv.model.TvWatchlistResponse

class TvWatchlistViewModel : ViewModel() {
    private lateinit var tvWatchlist: LiveData<TvWatchlistResponse>

    fun getTvWatchlist(accountId: Long, sessionId: String): LiveData<TvWatchlistResponse> {
        tvWatchlist = Api.getTvWatchlist(accountId, sessionId)

        return tvWatchlist
    }
}