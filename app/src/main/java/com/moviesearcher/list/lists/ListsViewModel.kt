package com.moviesearcher.list.lists

import androidx.lifecycle.ViewModel

class ListsViewModel(private val movieId: Long) : ViewModel() {
//    private val lists = MutableLiveData<Resource<ListsResponse>>()
//
//    init {
//        fetchLists()
//    }
//
//    private fun fetchLists() {
//        viewModelScope.launch {
//            lists.postValue(Resource.loading(null))
//            try {
//                val listsFromApi = ApiService.create().getCreatedLists(movieId)
//                lists.postValue(Resource.success(listsFromApi))
//            } catch (e: Exception) {
//                lists.postValue(Resource.error(e.toString(), null))
//            }
//        }
//    }
//
//    fun getLists(): MutableLiveData<Resource<ListsResponse>> {
//        return lists
//    }
}