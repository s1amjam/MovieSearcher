package com.moviesearcher.watchlist.common.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.watchlist.movie.model.MovieWatchlistResponse
import com.moviesearcher.watchlist.tv.model.TvWatchlistResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class WatchlistViewModel(private val accountId: Long, private val sessionId: String) : ViewModel() {
    private val movieWatchlist = MutableLiveData<Resource<MovieWatchlistResponse>>()
    private val tvWatchlist = MutableLiveData<Resource<TvWatchlistResponse>>()
    private val watchlistItemsIds = MutableLiveData<Resource<MutableList<Long>>>()

    private fun fetchMoviesWatchlist() {
        viewModelScope.launch {
            movieWatchlist.postValue(Resource.loading(null))
            try {
                val moviesWatchlistFromApi =
                    ApiService.create().getMovieWatchlist(accountId, sessionId)
                movieWatchlist.postValue(Resource.success(moviesWatchlistFromApi))
            } catch (e: Exception) {
                movieWatchlist.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getMovieWatchlist(): MutableLiveData<Resource<MovieWatchlistResponse>> {
        if (movieWatchlist.value == null) {
            fetchMoviesWatchlist()
        }

        return movieWatchlist
    }

    private fun fetchTvWatchlist() {
        viewModelScope.launch {
            tvWatchlist.postValue(Resource.loading(null))
            try {
                val tvWatchlistFromApi = ApiService.create().getTvWatchlist(accountId, sessionId)
                tvWatchlist.postValue(Resource.success(tvWatchlistFromApi))
            } catch (e: Exception) {
                tvWatchlist.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getTvWatchlist(): MutableLiveData<Resource<TvWatchlistResponse>> {
        if (tvWatchlist.value == null) {
            fetchTvWatchlist()
        }

        return tvWatchlist
    }

    private fun fetchWatchlistItemsIds() {
        viewModelScope.launch {
            watchlistItemsIds.postValue(Resource.loading(null))
            try {
                coroutineScope {
                    val moviesWatchlistFromApiDeferred =
                        async { ApiService.create().getMovieWatchlist(accountId, sessionId) }
                    val tvWatchlistFromApiDeferred =
                        async { ApiService.create().getTvWatchlist(accountId, sessionId) }

                    val moviesWatchlistFromApi = moviesWatchlistFromApiDeferred.await()
                    val tvWatchlistFromApi = tvWatchlistFromApiDeferred.await()

                    val allWatchlistIdsFromApi = mutableListOf<Long>()
                    moviesWatchlistFromApi.results?.forEach { it ->
                        allWatchlistIdsFromApi.add(it.id!!.toLong())
                    }
                    tvWatchlistFromApi.results?.forEach { it ->
                        allWatchlistIdsFromApi.add(it.id!!.toLong())
                    }

                    watchlistItemsIds.postValue(Resource.success(allWatchlistIdsFromApi))
                }
            } catch (e: Exception) {
                watchlistItemsIds.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getWatchlistItemsIds(): MutableLiveData<Resource<MutableList<Long>>> {
        if (watchlistItemsIds.value == null) {
            fetchWatchlistItemsIds()
        }

        return watchlistItemsIds
    }
}