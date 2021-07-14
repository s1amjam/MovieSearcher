package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.watchlist.tv.TvWatchlistResponse

class TvWatchlistViewModel : ViewModel() {
    lateinit var tvWatchlist: LiveData<TvWatchlistResponse>

    fun getTvWatchlist(accountId: Int, sessionId: String): LiveData<TvWatchlistResponse> {
        tvWatchlist = Api.getTvWatchlist(accountId, sessionId)

        return tvWatchlist
    }
}