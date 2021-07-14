package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.list.CheckItemStatusResponse
import com.moviesearcher.api.entity.list.ListsResponse

class MyListsViewModel : ViewModel() {
    lateinit var myLists: LiveData<ListsResponse>
    lateinit var checkedItem: LiveData<CheckItemStatusResponse>

    fun getLists(accountId: Int, sessionId: String, page: Int): LiveData<ListsResponse> {
        myLists = Api.getLists(accountId, sessionId, page)

        return myLists
    }

    fun checkItemStatus(listId: Int, movieId: Long): LiveData<CheckItemStatusResponse> {
        checkedItem = Api.checkItemStatus(listId, movieId)

        return checkedItem
    }
}