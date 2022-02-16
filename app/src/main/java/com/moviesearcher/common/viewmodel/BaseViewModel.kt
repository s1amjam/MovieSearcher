package com.moviesearcher.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.list.lists.model.ListsResponse
import com.moviesearcher.list.model.CheckItemStatusResponse
import com.moviesearcher.list.model.ListResponse
import com.moviesearcher.rated.movie.model.RatedMoviesResponse
import com.moviesearcher.rated.tv.model.RatedTvsResponse
import com.moviesearcher.rated.tvepisode.model.RatedTvEpisodesResponse
import com.moviesearcher.search.model.SearchResponse

class BaseViewModel : ViewModel() {
    private val searchItem: LiveData<SearchResponse>
    private var mutableQuery: MutableLiveData<String> = MutableLiveData()
    private lateinit var myLists: LiveData<ListsResponse>
    private lateinit var checkedItem: LiveData<CheckItemStatusResponse>
    private lateinit var myList: LiveData<ListResponse>
    private lateinit var ratedMovies: LiveData<RatedMoviesResponse>
    private lateinit var ratedTvs: LiveData<RatedTvsResponse>
    private lateinit var ratedTvEpisodes: LiveData<RatedTvEpisodesResponse>

    init {
        searchItem = Transformations.switchMap(mutableQuery) { query ->
            Api.search(query)
        }
    }

    fun queryForSearch(query: String): LiveData<SearchResponse> {
        mutableQuery.value = query

        return searchItem
    }

    fun getRatedTvEpisodes(accountId: Long, sessionId: String): LiveData<RatedTvEpisodesResponse> {
        ratedTvEpisodes = Api.getRatedTvEpisodes(accountId, sessionId)

        return ratedTvEpisodes
    }

    fun getRatedTvs(accountId: Long, sessionId: String): LiveData<RatedTvsResponse> {
        ratedTvs = Api.getRatedTvs(accountId, sessionId)

        return ratedTvs
    }

    fun getLists(accountId: Long, sessionId: String, page: Int): LiveData<ListsResponse> {
        myLists = Api.getLists(accountId, sessionId, page)

        return myLists
    }

    fun checkItemStatus(listId: Int, movieId: Long): LiveData<CheckItemStatusResponse> {
        checkedItem = Api.checkItemStatus(listId, movieId)

        return checkedItem
    }

    fun getList(listId: Int): LiveData<ListResponse> {
        myList = Api.getListInfo(listId)

        return myList
    }

    fun getRatedMovies(accountId: Long, sessionId: String): LiveData<RatedMoviesResponse> {
        ratedMovies = Api.getRatedMovies(accountId, sessionId)

        return ratedMovies
    }
}