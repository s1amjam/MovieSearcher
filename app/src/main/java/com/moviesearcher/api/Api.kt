package com.moviesearcher.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.moviesearcher.api.entity.moviedetails.MovieDetailsResponse
import com.moviesearcher.api.entity.trending.TrendingResponse
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
                Log.d(TAG, call.request().toString())
                Log.d(TAG, response.body()?.toString()!!)
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

    fun getMovieDetails(
        authToken: String,
        movieId: Int
    ): MutableLiveData<MovieDetailsResponse> {
        val responseLiveData: MutableLiveData<MovieDetailsResponse> = MutableLiveData()

        val resp = MovieSearcherApiService.create()
            .movieDetails(
                authToken,
                movieId
            )

        resp.enqueue(object : Callback<MovieDetailsResponse> {
            override fun onResponse(
                call: Call<MovieDetailsResponse>,
                response: Response<MovieDetailsResponse>
            ) {
                Log.d(TAG, call.request().toString())
                Log.d(TAG, response.body()?.toString()!!)
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<MovieDetailsResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }
}