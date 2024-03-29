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
import com.moviesearcher.common.credentials.CredentialsHolder
import com.moviesearcher.common.model.common.ResponseWithCodeAndMessage
import com.moviesearcher.common.utils.Constants.ERROR_MESSAGE
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.common.utils.Status
import com.moviesearcher.watchlist.common.model.WatchlistRequest
import com.moviesearcher.watchlist.movie.model.MovieWatchlistResponse
import com.moviesearcher.watchlist.tv.model.TvWatchlistResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(private val credentialsHolder: CredentialsHolder) :
    ViewModel() {

    private val movieWatchlist = MutableLiveData<Resource<MovieWatchlistResponse>>()
    private val tvWatchlist = MutableLiveData<Resource<TvWatchlistResponse>>()
    private val watchlistItemsIds = MutableLiveData<Resource<MutableList<Long>>>()
    private val watchlist = MutableLiveData<Resource<ResponseWithCodeAndMessage>>()

    private val watchlistAddedIcon = R.drawable.ic_watchlist_added_36
    private val watchlistRemovedIcon = R.drawable.ic_watchlist_add_36

    private fun fetchMoviesWatchlist() {
        viewModelScope.launch {
            movieWatchlist.postValue(Resource.loading(null))
            try {
                val moviesWatchlistFromApi =
                    ApiService.create().getMovieWatchlist(
                        credentialsHolder.getAccountId(),
                        credentialsHolder.getSessionId()
                    )
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

    fun postWatchlist(
        watchlistRequest: WatchlistRequest
    ): MutableLiveData<Resource<ResponseWithCodeAndMessage>> {
        fetchWatchlist(
            credentialsHolder.getAccountId(),
            credentialsHolder.getSessionId(),
            watchlistRequest
        )

        return watchlist
    }

    private fun fetchTvWatchlist() {
        viewModelScope.launch {
            tvWatchlist.postValue(Resource.loading(null))
            try {
                val tvWatchlistFromApi = ApiService.create().getTvWatchlist(
                    credentialsHolder.getAccountId(),
                    credentialsHolder.getSessionId()
                )
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

    private fun fetchWatchlistItemsIds(isTv: Boolean) {
        viewModelScope.launch {
            watchlistItemsIds.postValue(Resource.loading(null))
            try {
                coroutineScope {
                    val watchlistIdsFromApi = mutableListOf<Long>()

                    if (isTv) {
                        val tvWatchlistFromApi =
                            ApiService.create().getTvWatchlist(
                                credentialsHolder.getAccountId(),
                                credentialsHolder.getSessionId()
                            )

                        tvWatchlistFromApi.results?.forEach { it ->
                            watchlistIdsFromApi.add(it.id!!.toLong())
                        }
                    } else {
                        val moviesWatchlistFromApi =
                            ApiService.create().getMovieWatchlist(
                                credentialsHolder.getAccountId(),
                                credentialsHolder.getSessionId()
                            )

                        moviesWatchlistFromApi.results?.forEach { it ->
                            watchlistIdsFromApi.add(it.id!!.toLong())
                        }
                    }

                    watchlistItemsIds.postValue(Resource.success(watchlistIdsFromApi))
                }
            } catch (e: Exception) {
                watchlistItemsIds.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    private fun getWatchlistItemsIds(isTv: Boolean): MutableLiveData<Resource<MutableList<Long>>> {
        if (watchlistItemsIds.value == null) {
            fetchWatchlistItemsIds(isTv)
        }

        return watchlistItemsIds
    }

    fun processWatchlistButtons(button: ImageButton) {
        if (button.tag.toString().toBoolean()) {
            button.tag = "false"
            button.setImageResource(watchlistAddedIcon)
        } else {
            button.tag = "true"
            button.setImageResource(watchlistRemovedIcon)
        }
    }

    fun checkWatchlist(
        button: ImageButton,
        media: MutableMap<String, Long>? = mutableMapOf(),
        lifecycleOwner: LifecycleOwner,
        context: Context,
        isTv: Boolean = false
    ) {
        if (credentialsHolder.getSessionId().isNotBlank()) {
            getWatchlistItemsIds(isTv).observe(lifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { movieItems ->
                            button.setImageResource(watchlistRemovedIcon)

                            if (movieItems.contains(media?.values?.first())) {
                                button.tag = "false"
                                button.setImageResource(watchlistAddedIcon)
                            } else {
                                button.tag = "true"
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