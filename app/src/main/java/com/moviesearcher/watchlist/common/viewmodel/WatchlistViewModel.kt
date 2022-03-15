package com.moviesearcher.watchlist.common.viewmodel

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
import com.moviesearcher.watchlist.common.model.WatchlistRequest
import com.moviesearcher.watchlist.movie.model.MovieWatchlistResponse
import com.moviesearcher.watchlist.tv.model.TvWatchlistResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

const val ERROR_MESSAGE = "Something went wrong '%s'"

class WatchlistViewModel(private val accountId: Long, private val sessionId: String) : ViewModel() {
    private val movieWatchlist = MutableLiveData<Resource<MovieWatchlistResponse>>()
    private val tvWatchlist = MutableLiveData<Resource<TvWatchlistResponse>>()
    private val watchlistItemsIds = MutableLiveData<Resource<MutableList<Long>>>()
    private val watchlist = MutableLiveData<Resource<ResponseWithCodeAndMessage>>()

    private val watchlistAddedIcon = R.drawable.ic_watchlist_added_36
    private val watchlistRemovedIcon = R.drawable.ic_watchlist_add_36

    private var isWatchlist = true

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

    private fun fetchWatchlist(
        accountId: Long,
        sessionId: String,
        watchlistRequest: WatchlistRequest
    ) {
        viewModelScope.launch {
            watchlist.postValue(Resource.loading(null))
            try {
                val watchlistFromApi =
                    ApiService.create().watchlist(accountId, sessionId, watchlistRequest)
                watchlist.postValue(Resource.success(watchlistFromApi))
            } catch (e: Exception) {
                watchlist.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    private fun postWatchlist(
        accountId: Long,
        sessionId: String,
        watchlistRequest: WatchlistRequest
    ): MutableLiveData<Resource<ResponseWithCodeAndMessage>> {
        fetchWatchlist(accountId, sessionId, watchlistRequest)

        return watchlist
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

    private fun getWatchlistItemsIds(): MutableLiveData<Resource<MutableList<Long>>> {
        if (watchlistItemsIds.value == null) {
            fetchWatchlistItemsIds()
        }

        return watchlistItemsIds
    }

    fun addToWatchlist(
        button: ImageButton,
        media: MutableMap<String, Long> = mutableMapOf(),
        context: Context,
        lifecycleOwner: LifecycleOwner
    ) {
        if (button.tag != null) {
            isWatchlist = button.tag.toString().toBoolean()
            button.tag = null //need to return to normal 'isWatchlist' cycle
        }

        postWatchlist(
            accountId,
            sessionId,
            WatchlistRequest(isWatchlist, media.values.first(), media.keys.first())
        ).observe(lifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                        if (isWatchlist) {
                            button.setImageResource(watchlistAddedIcon)
                            Toast.makeText(context, "Added to Watchlist", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            button.setImageResource(watchlistRemovedIcon)
                            Toast.makeText(context, "Removed from Watchlist", Toast.LENGTH_SHORT)
                                .show()
                        }

                        isWatchlist = !isWatchlist
                    }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    Toast.makeText(
                        context,
                        ERROR_MESSAGE.format(
                            "Error adding to Watchlist, try again later" + it.message
                        ),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun checkWatchlist(
        button: ImageButton,
        media: MutableMap<String, Long>? = mutableMapOf(),
        lifecycleOwner: LifecycleOwner,
        context: Context
    ) {
        if (sessionId.isNotBlank()) {
            getWatchlistItemsIds().observe(lifecycleOwner) { it ->
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { movieItems ->
                            button.setImageResource(watchlistRemovedIcon)
                            movieItems.forEach {
                                if (it == media?.values?.first()) {
                                    isWatchlist = false
                                    button.tag = "false"
                                    button.setImageResource(watchlistAddedIcon)
                                }
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
}