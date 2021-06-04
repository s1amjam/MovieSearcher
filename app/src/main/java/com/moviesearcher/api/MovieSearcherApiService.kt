package com.moviesearcher.api

import com.moviesearcher.Constants
import com.moviesearcher.api.entity.moviedetails.MovieDetailsResponse
import com.moviesearcher.api.entity.trending.TrendingResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface MovieSearcherApiService {

    companion object Factory {
        fun create(): MovieSearcherApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build()

            return retrofit.create(MovieSearcherApiService::class.java)
        }
    }

    @GET("trending/{media_type}/{time_window}")
    fun trending(
        @Header("Authorization") bearerToken: String,
        @Path("media_type") mediaType: String,
        @Path("time_window") timeWindow: String
    ): Call<TrendingResponse>

    @GET("movie/{movie_id}")
    fun movieDetails(
        @Header("Authorization") bearerToken: String,
        @Path("movie_id") movieId: Int
    ): Call<MovieDetailsResponse>
}