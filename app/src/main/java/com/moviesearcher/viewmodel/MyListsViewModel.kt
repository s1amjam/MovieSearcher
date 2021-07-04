package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.list.ListsResponse

class MyListsViewModel : ViewModel() {
    lateinit var myListsItemLiveData: LiveData<ListsResponse>

    fun getLists(accountId: Int, sessionId: String, page: Int) {
        myListsItemLiveData = Api.getLists(accountId, sessionId, page)
    }
}