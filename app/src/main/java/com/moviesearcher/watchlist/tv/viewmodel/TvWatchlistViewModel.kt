package com.moviesearcher.watchlist.tv.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.watchlist.tv.model.TvWatchlistResponse

class TvWatchlistViewModel : ViewModel() {
    private lateinit var _tvWatchlist: MutableLiveData<TvWatchlistResponse>
    val tvWatchlist: LiveData<TvWatchlistResponse> get() = _tvWatchlist

    fun getTvWatchlist(accountId: Long, sessionId: String): LiveData<TvWatchlistResponse> {
        _tvWatchlist = Api.getTvWatchlist(accountId, sessionId)

        return tvWatchlist
    }

    fun getTvWatchlistIds(): MutableList<Long> {
        val tvWatchlistIds: MutableList<Long> = mutableListOf()
        _tvWatchlist.value?.results?.forEach { it -> tvWatchlistIds.add(it.id!!.toLong()) }

        return tvWatchlistIds
    }
}