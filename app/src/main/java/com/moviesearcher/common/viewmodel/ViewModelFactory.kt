package com.moviesearcher.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moviesearcher.watchlist.common.viewmodel.WatchlistViewModel

class ViewModelFactory(private val sessionId: String?, private val accountId: Long?) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WatchlistViewModel::class.java)) {
            return WatchlistViewModel(accountId!!, sessionId!!) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}