package com.moviesearcher.list.lists.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.list.lists.model.ListsResponse
import com.moviesearcher.list.model.CheckItemStatusResponse

class MyListsViewModel : ViewModel() {
    lateinit var myLists: LiveData<ListsResponse>
    lateinit var checkedItem: LiveData<CheckItemStatusResponse>

    fun getLists(accountId: Long, sessionId: String, page: Int): LiveData<ListsResponse> {
        myLists = Api.getLists(accountId, sessionId, page)

        return myLists
    }

    fun checkItemStatus(listId: Int, movieId: Long): LiveData<CheckItemStatusResponse> {
        checkedItem = Api.checkItemStatus(listId, movieId)

        return checkedItem
    }
}