package com.moviesearcher.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.list.lists.model.ListsResponse
import com.moviesearcher.list.model.CheckItemStatusResponse
import com.moviesearcher.list.model.ListResponse

class BaseViewModel : ViewModel() {
    private lateinit var myLists: LiveData<ListsResponse>
    private lateinit var checkedItem: LiveData<CheckItemStatusResponse>
    private lateinit var myList: LiveData<ListResponse>

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