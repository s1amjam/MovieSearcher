package com.moviesearcher.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.moviesearcher.api.entity.auth.CreateSessionResponse
import com.moviesearcher.api.entity.auth.CreateTokenResponse
import com.moviesearcher.api.entity.auth.RequestToken
import com.moviesearcher.api.entity.movieinfo.MovieInfoResponse
import com.moviesearcher.api.entity.search.SearchResponse
import com.moviesearcher.api.entity.trending.TrendingResponse
import com.moviesearcher.api.entity.tvinfo.TvInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "Api"

object Api {

    fun getTrending(mediaType: String, timeWindow: String): MutableLiveData<TrendingResponse> {
        val responseLiveData: MutableLiveData<TrendingResponse> = MutableLiveData()

        val resp = ApiService.create().trending(mediaType, timeWindow)

        resp.enqueue(object : Callback<TrendingResponse> {
            override fun onResponse(
                call: Call<TrendingResponse>,
                response: Response<TrendingResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<TrendingResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun getMovieInfo(movieId: Int): MutableLiveData<MovieInfoResponse> {
        val responseLiveData: MutableLiveData<MovieInfoResponse> = MutableLiveData()

        val resp = ApiService.create().movieInfo(movieId)

        resp.enqueue(object : Callback<MovieInfoResponse> {
            override fun onResponse(
                call: Call<MovieInfoResponse>,
                response: Response<MovieInfoResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<MovieInfoResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun getTvInfo(tvId: Int): MutableLiveData<TvInfoResponse> {
        val responseLiveData: MutableLiveData<TvInfoResponse> = MutableLiveData()

        val resp = ApiService.create().tvInfo(tvId)

        resp.enqueue(object : Callback<TvInfoResponse> {
            override fun onResponse(
                call: Call<TvInfoResponse>,
                response: Response<TvInfoResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<TvInfoResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun search(query: String): MutableLiveData<SearchResponse> {
        val responseLiveData: MutableLiveData<SearchResponse> = MutableLiveData()

        val resp = ApiService.create().search(query)

        resp.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<SearchResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

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

    fun createSession(requestToken: RequestToken): CreateSessionResponse? {
        val resp = ApiService.create().createSession(requestToken = requestToken)
        var enqueuedResponse: CreateSessionResponse? = null

        resp.enqueue(object : Callback<CreateSessionResponse> {
            override fun onResponse(
                call: Call<CreateSessionResponse>,
                response: Response<CreateSessionResponse>
            ) {
                Log.d(TAG, call.toString())
                Log.d(TAG, response.body().toString())
                Log.d(TAG, response.errorBody().toString())
                Log.d(TAG, response.message().toString())
                enqueuedResponse = response.body()!!
            }

            override fun onFailure(
                call: Call<CreateSessionResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return enqueuedResponse
    }
}