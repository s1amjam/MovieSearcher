package com.moviesearcher.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.list.model.CheckItemStatusResponse
import com.moviesearcher.list.model.ListResponse
import kotlinx.coroutines.launch

class ListViewModel(private val listId: Int? = null, private val movieId: Long? = null) : ViewModel() {
    private val checkedItem = MutableLiveData<Resource<CheckItemStatusResponse>>()
    private val myList = MutableLiveData<Resource<ListResponse>>()

    init {
        fetchCheckedItem()
        fetchMyList()
    }

    private fun fetchCheckedItem() {
        viewModelScope.launch {
            checkedItem.postValue(Resource.loading(null))
            try {
                val movieCastFromApi = ApiService.create().checkItemStatus(listId!!, movieId!!)
                checkedItem.postValue(Resource.success(movieCastFromApi))
            } catch (e: Exception) {
                checkedItem.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getCheckedItem(): MutableLiveData<Resource<CheckItemStatusResponse>> {
        return checkedItem
    }

    private fun fetchMyList() {
        viewModelScope.launch {
            myList.postValue(Resource.loading(null))
            try {
                val recommendationsFromApi = ApiService.create().getListInfo(listId!!)
                myList.postValue(Resource.success(recommendationsFromApi))
            } catch (e: Exception) {
                myList.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getMyList(): MutableLiveData<Resource<ListResponse>> {
        return myList
    }
}