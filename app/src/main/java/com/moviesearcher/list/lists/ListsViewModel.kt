package com.moviesearcher.list.lists

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.credentials.CredentialsHolder
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.list.lists.model.ListsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListsViewModel @Inject constructor(private val credentialsHolder: CredentialsHolder) :
    ViewModel() {
    private val lists = MutableLiveData<Resource<ListsResponse>>()

    init {
        fetchLists()
    }

    private fun fetchLists() {
        viewModelScope.launch {
            lists.postValue(Resource.loading(null))
            try {
                val listsFromApi = ApiService.create().getCreatedLists(
                    credentialsHolder.getAccountId(),
                    credentialsHolder.getSessionId(),
                    1
                )
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