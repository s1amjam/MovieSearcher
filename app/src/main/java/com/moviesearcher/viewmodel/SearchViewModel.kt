package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.search.SearchResponse

class SearchViewModel : ViewModel() {
    private val searchItem: LiveData<SearchResponse>
    private var mutableQuery: MutableLiveData<String> = MutableLiveData()

    init {
        searchItem = Transformations.switchMap(mutableQuery) { query ->
            Api.search(query)
        }
    }

    fun queryForSearch(query: String): LiveData<SearchResponse> {
        mutableQuery.value = query

        return searchItem
    }
}