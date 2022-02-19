package com.moviesearcher.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.list.lists.model.ListsResponse
import com.moviesearcher.list.model.CheckItemStatusResponse
import com.moviesearcher.list.model.ListResponse
import com.moviesearcher.search.model.SearchResponse

class BaseViewModel : ViewModel() {
    private val searchItem: LiveData<SearchResponse>
    private var mutableQuery: MutableLiveData<String> = MutableLiveData()
    private lateinit var myLists: LiveData<ListsResponse>
    private lateinit var checkedItem: LiveData<CheckItemStatusResponse>
    private lateinit var myList: LiveData<ListResponse>

    init {
        searchItem = Transformations.switchMap(mutableQuery) { query ->
            Api.search(query)
        }
    }

    fun queryForSearch(query: String): LiveData<SearchResponse> {
        mutableQuery.value = query

        return searchItem
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
}