package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.list.ListResponse

class MyListViewModel : ViewModel() {
    lateinit var myLists: LiveData<ListResponse>

    fun getList(listId: Int): LiveData<ListResponse> {
        myLists = Api.getListInfo(listId)

        return myLists
    }
}