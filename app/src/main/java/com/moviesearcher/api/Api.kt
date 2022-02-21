package com.moviesearcher.api

import androidx.lifecycle.MutableLiveData
import com.moviesearcher.common.model.account.AccountResponse
import com.moviesearcher.common.model.auth.CreateSessionResponse
import com.moviesearcher.common.model.auth.CreateTokenResponse
import com.moviesearcher.common.model.auth.DeleteSessionResponse
import com.moviesearcher.common.model.auth.RequestToken
import com.moviesearcher.common.model.auth.SessionId
import com.moviesearcher.common.model.common.MediaId
import com.moviesearcher.common.model.common.ResponseWithCodeAndMessage
import com.moviesearcher.favorite.model.MarkAsFavoriteRequest
import com.moviesearcher.list.lists.model.ListsResponse
import com.moviesearcher.list.model.CreateNewList
import com.moviesearcher.list.model.CreateNewListResponse
import com.moviesearcher.list.model.add.AddToListResponse
import com.moviesearcher.watchlist.common.model.WatchlistRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "Api"

object Api {

    fun createRequestToken(): MutableLiveData<CreateTokenResponse> {
        val resp = ApiService.create().newRequestToken()
        val responseLiveData: MutableLiveData<CreateTokenResponse> = MutableLiveData()

        resp.enqueue(object : Callback<CreateTokenResponse> {
            override fun onResponse(
                call: Call<CreateTokenResponse>,
                response: Response<CreateTokenResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<CreateTokenResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun createSession(requestToken: RequestToken): MutableLiveData<CreateSessionResponse> {
        val resp = ApiService.create().createSession(requestToken = requestToken)
        val responseLiveData: MutableLiveData<CreateSessionResponse> = MutableLiveData()

        resp.enqueue(object : Callback<CreateSessionResponse> {
            override fun onResponse(
                call: Call<CreateSessionResponse>,
                response: Response<CreateSessionResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<CreateSessionResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun deleteSession(sessionId: SessionId): MutableLiveData<DeleteSessionResponse> {
        val resp = ApiService.create().deleteSession(sessionId = sessionId)
        val responseLiveData: MutableLiveData<DeleteSessionResponse> = MutableLiveData()

        resp.enqueue(object : Callback<DeleteSessionResponse> {
            override fun onResponse(
                call: Call<DeleteSessionResponse>,
                response: Response<DeleteSessionResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<DeleteSessionResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun getAccount(sessionId: String?): MutableLiveData<AccountResponse> {
        val responseLiveData: MutableLiveData<AccountResponse> = MutableLiveData()
        val resp = ApiService.create().getAccount(sessionId!!)

        resp.enqueue(object : Callback<AccountResponse> {
            override fun onResponse(
                call: Call<AccountResponse>,
                response: Response<AccountResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<AccountResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun getLists(accountId: Long?, sessionId: String?, page: Int): MutableLiveData<ListsResponse> {
        val responseLiveData: MutableLiveData<ListsResponse> = MutableLiveData()
        val resp = ApiService.create().getCreatedLists(accountId, sessionId, page)

        resp.enqueue(object : Callback<ListsResponse> {
            override fun onResponse(
                call: Call<ListsResponse>,
                response: Response<ListsResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<ListsResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun addToList(
        listId: Int,
        mediaId: MediaId,
        sessionId: String
    ): MutableLiveData<AddToListResponse> {
        val responseLiveData: MutableLiveData<AddToListResponse> = MutableLiveData()
        val resp = ApiService.create().addToList(listId, sessionId, mediaId)

        resp.enqueue(object : Callback<AddToListResponse> {
            override fun onResponse(
                call: Call<AddToListResponse>,
                response: Response<AddToListResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<AddToListResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun createNewList(
        sessionId: String,
        createNewList: CreateNewList
    ): MutableLiveData<CreateNewListResponse> {
        val responseLiveData: MutableLiveData<CreateNewListResponse> = MutableLiveData()
        val resp = ApiService.create().createNewList(sessionId, createNewList)

        resp.enqueue(object : Callback<CreateNewListResponse> {
            override fun onResponse(
                call: Call<CreateNewListResponse>,
                response: Response<CreateNewListResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<CreateNewListResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun removeFromList(
        listId: Int,
        sessionId: String,
        mediaId: MediaId
    ): MutableLiveData<ResponseWithCodeAndMessage> {
        val responseLiveData: MutableLiveData<ResponseWithCodeAndMessage> = MutableLiveData()
        val resp = ApiService.create().removeFromList(listId, sessionId, mediaId)

        resp.enqueue(object : Callback<ResponseWithCodeAndMessage> {
            override fun onResponse(
                call: Call<ResponseWithCodeAndMessage>,
                response: Response<ResponseWithCodeAndMessage>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<ResponseWithCodeAndMessage>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun deleteList(
        listId: Int,
        sessionId: String
    ): MutableLiveData<ResponseWithCodeAndMessage> {
        val responseLiveData: MutableLiveData<ResponseWithCodeAndMessage> = MutableLiveData()
        val resp = ApiService.create().deleteList(listId, sessionId)

        resp.enqueue(object : Callback<ResponseWithCodeAndMessage> {
            override fun onResponse(
                call: Call<ResponseWithCodeAndMessage>,
                response: Response<ResponseWithCodeAndMessage>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<ResponseWithCodeAndMessage>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun markAsFavorite(
        accountId: Long,
        sessionId: String,
        markAsFavorite: MarkAsFavoriteRequest
    ): MutableLiveData<ResponseWithCodeAndMessage> {
        val responseLiveData: MutableLiveData<ResponseWithCodeAndMessage> = MutableLiveData()
        val resp = ApiService.create().markAsFavorite(accountId, sessionId, markAsFavorite)

        resp.enqueue(object : Callback<ResponseWithCodeAndMessage> {
            override fun onResponse(
                call: Call<ResponseWithCodeAndMessage>,
                response: Response<ResponseWithCodeAndMessage>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<ResponseWithCodeAndMessage>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun watchlist(
        accountId: Long,
        sessionId: String,
        watchlist: WatchlistRequest
    ): MutableLiveData<ResponseWithCodeAndMessage> {
        val responseLiveData: MutableLiveData<ResponseWithCodeAndMessage> = MutableLiveData()
        val resp = ApiService.create().watchlist(accountId, sessionId, watchlist)

        resp.enqueue(object : Callback<ResponseWithCodeAndMessage> {
            override fun onResponse(
                call: Call<ResponseWithCodeAndMessage>,
                response: Response<ResponseWithCodeAndMessage>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<ResponseWithCodeAndMessage>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }
}