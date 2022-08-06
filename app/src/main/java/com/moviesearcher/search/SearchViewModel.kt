package com.moviesearcher.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.search.model.SearchResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val searchResult = MutableLiveData<Resource<SearchResponse>>()
    private var mutableQuery: MutableLiveData<String> = MutableLiveData()

    private fun fetchSearchResults(query: String) {
        viewModelScope.launch {
            searchResult.postValue(Resource.loading(null))
            try {
                val personFromApi = ApiService.create().search(query)
                searchResult.postValue(Resource.success(personFromApi))
            } catch (e: Exception) {
                searchResult.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun queryForSearch(query: String) {
        mutableQuery.value = query
    }

    fun getSearchResult(): MutableLiveData<Resource<SearchResponse>> {
        fetchSearchResults(mutableQuery.value.toString())

        return searchResult
    }
}