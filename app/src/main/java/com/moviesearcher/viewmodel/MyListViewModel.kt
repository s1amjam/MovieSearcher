package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.list.ListResponse

class MyListViewModel : ViewModel() {
    lateinit var myListItemLiveData: LiveData<ListResponse>

    fun getList(listId: Int) {
        myListItemLiveData = Api.getListInfo(listId)
    }
}