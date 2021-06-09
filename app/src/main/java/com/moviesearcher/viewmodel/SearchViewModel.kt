package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.search.SearchResponse
import com.moviesearcher.utils.Constants.ACCESS_TOKEN

class SearchViewModel(query: String) {
    val searchItemLiveData: LiveData<SearchResponse>
    lateinit var mutableQuery: MutableLiveData<String>

    init {
        searchItemLiveData = Transformations.switchMap(mutableQuery) { query ->
            Api.search(
                ACCESS_TOKEN,
                query
            )
        }
    }

    fun queryForSearch(query: String) {
        mutableQuery.value = query
    }
}