package com.moviesearcher.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.moviesearcher.entity.TrendingResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object Api {

    fun getTrending(
        authToken: String,
        mediaType: String,
        timeWindow: String
    ): MutableLiveData<TrendingResponse> {
        val responseLiveData: MutableLiveData<TrendingResponse> = MutableLiveData()

        val resp = MovieSearcherApiService.create()
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
                Log.d("asdF", call.request().toString())
                Log.d("asdF", response.body()?.toString()!!)
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
}