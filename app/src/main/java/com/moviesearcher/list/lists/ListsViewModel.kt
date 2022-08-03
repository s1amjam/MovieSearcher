package com.moviesearcher.list.lists

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.list.lists.model.ListsResponse
import kotlinx.coroutines.launch

class ListsViewModel(
    private val accountId: Long,
    private val sessionId: String,
    private val page: Int
) : ViewModel() {
    private val lists = MutableLiveData<Resource<ListsResponse>>()

    init {
        fetchLists()
    }

    private fun fetchLists() {
        viewModelScope.launch {
            lists.postValue(Resource.loading(null))
            try {
                val listsFromApi = ApiService.create().getCreatedLists(accountId, sessionId, page)
                lists.postValue(Resource.success(listsFromApi))
            } catch (e: Exception) {
                lists.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getLists(): MutableLiveData<Resource<ListsResponse>> {
        return lists
    }
}