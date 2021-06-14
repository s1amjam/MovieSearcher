package com.moviesearcher.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.moviesearcher.api.entity.movieinfo.MovieInfoResponse
import com.moviesearcher.api.entity.search.SearchResponse
import com.moviesearcher.api.entity.trending.TrendingResponse
import com.moviesearcher.api.entity.tvinfo.TvInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "Api"

object Api {

    fun getTrending(
        authToken: String,
        mediaType: String,
        timeWindow: String
    ): MutableLiveData<TrendingResponse> {
        val responseLiveData: MutableLiveData<TrendingResponse> = MutableLiveData()

        val resp = ApiService.create()
            .trending(
                authToken,
                mediaType,
                timeWindow
            )

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

    fun getMovieInfo(
        authToken: String,
        movieId: Int
    ): MutableLiveData<MovieInfoResponse> {
        val responseLiveData: MutableLiveData<MovieInfoResponse> = MutableLiveData()

        val resp = ApiService.create()
            .movieInfo(
                authToken,
                movieId
            )

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

    fun getTvInfo(
        authToken: String,
        tvId: Int
    ): MutableLiveData<TvInfoResponse> {
        val responseLiveData: MutableLiveData<TvInfoResponse> = MutableLiveData()

        val resp = ApiService.create()
            .tvInfo(
                authToken,
                tvId
            )

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

    fun search(
        authToken: String,
        query: String
    ): MutableLiveData<SearchResponse> {
        val responseLiveData: MutableLiveData<SearchResponse> = MutableLiveData()

        val resp = ApiService.create()
            .search(
                authToken,
                query
            )

        resp.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                Log.d(TAG, call.request().toString())
                Log.d(TAG, response.body().toString())
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
}