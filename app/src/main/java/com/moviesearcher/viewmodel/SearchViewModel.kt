package com.moviesearcher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.search.SearchResponse
import com.moviesearcher.utils.Constants.ACCESS_TOKEN

class SearchViewModel : ViewModel() {
    val searchItemLiveData: LiveData<SearchResponse>
    private var mutableQuery: MutableLiveData<String> = MutableLiveData()

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