package com.moviesearcher.api

import androidx.lifecycle.MutableLiveData
import com.moviesearcher.common.model.common.MediaId
import com.moviesearcher.common.model.common.ResponseWithCodeAndMessage
import com.moviesearcher.favorite.model.MarkAsFavoriteRequest
import com.moviesearcher.watchlist.common.model.WatchlistRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "Api"

object Api {
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