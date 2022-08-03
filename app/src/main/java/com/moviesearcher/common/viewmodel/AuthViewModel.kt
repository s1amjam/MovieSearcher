package com.moviesearcher.common.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.model.account.AccountResponse
import com.moviesearcher.common.model.auth.*
import com.moviesearcher.common.utils.Resource
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val requestToken = MutableLiveData<Resource<CreateTokenResponse>>()
    private val createSession = MutableLiveData<Resource<CreateSessionResponse>>()
    private val account = MutableLiveData<Resource<AccountResponse>>()
    private val deleteSession = MutableLiveData<Resource<DeleteSessionResponse>>()

    private fun fetchToken() {
        viewModelScope.launch {
            requestToken.postValue(Resource.loading(null))
            try {
                val tokenFromApi = ApiService.create().newRequestToken()
                requestToken.postValue(Resource.success(tokenFromApi))
            } catch (e: Exception) {
                requestToken.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getToken(): MutableLiveData<Resource<CreateTokenResponse>> {
        fetchToken()
        return requestToken
    }

    private fun fetchSession(requestToken: RequestToken) {
        viewModelScope.launch {
            createSession.postValue(Resource.loading(null))
            try {
                val sessionFromApi = ApiService.create().createSession(requestToken)
                createSession.postValue(Resource.success(sessionFromApi))
            } catch (e: Exception) {
                createSession.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getSession(requestToken: RequestToken): MutableLiveData<Resource<CreateSessionResponse>> {
        fetchSession(requestToken)

        return createSession
    }

    private fun fetchAccount(sessionId: String) {
        viewModelScope.launch {
            account.postValue(Resource.loading(null))
            try {
                val accountFromApi = ApiService.create().getAccount(sessionId)
                account.postValue(Resource.success(accountFromApi))
            } catch (e: Exception) {
                account.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getAccount(sessionId: String): MutableLiveData<Resource<AccountResponse>> {
        fetchAccount(sessionId)

        return account
    }

    private fun deleteSession(sessionId: SessionId) {
        viewModelScope.launch {
            deleteSession.postValue(Resource.loading(null))
            try {
                val accountFromApi = ApiService.create().deleteSession(sessionId)
                deleteSession.postValue(Resource.success(accountFromApi))
            } catch (e: Exception) {
                deleteSession.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getDeleteSession(sessionId: SessionId): MutableLiveData<Resource<DeleteSessionResponse>> {
        deleteSession(sessionId)

        return deleteSession
    }
}