package com.moviesearcher.common.viewmodel.accountstates

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.moviesearcher.api.Api
import com.moviesearcher.common.model.accountstates.AccountStatesResponse

class AccountStatesViewModel : ViewModel() {
    private lateinit var movieAccountStates: LiveData<AccountStatesResponse>
    private lateinit var tvAccountStates: LiveData<AccountStatesResponse>

    fun getTvAccountStates(
        tvId: Long,
        sessionId: String
    ): LiveData<AccountStatesResponse> {
        tvAccountStates = Api.getTvAccountStates(tvId, sessionId)

        return tvAccountStates
    }

    fun getMovieAccountStates(
        movieId: Long,
        sessionId: String
    ): LiveData<AccountStatesResponse> {
        movieAccountStates = Api.getMovieAccountStates(movieId, sessionId)

        return movieAccountStates
    }
}