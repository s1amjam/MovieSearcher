package com.moviesearcher.api

import com.moviesearcher.api.entity.movieinfo.MovieInfoResponse
import com.moviesearcher.api.entity.search.SearchResponse
import com.moviesearcher.api.entity.trending.TrendingResponse
import com.moviesearcher.api.entity.tvinfo.TvInfoResponse
import com.moviesearcher.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    companion object {
        fun create(): ApiService {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            val retrofit = Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }

    @GET("trending/{media_type}/{time_window}")
    fun trending(
        @Header("Authorization") bearerToken: String,
        @Path("media_type") mediaType: String,
        @Path("time_window") timeWindow: String
    ): Call<TrendingResponse>

    @GET("movie/{movie_id}")
    fun movieInfo(
        @Header("Authorization") bearerToken: String,
        @Path("movie_id") movieId: Int
    ): Call<MovieInfoResponse>

    @GET("tv/{tv_id}")
    fun tvInfo(
        @Header("Authorization") bearerToken: String,
        @Path("tv_id") tvId: Int
    ): Call<TvInfoResponse>

    @GET("search/multi")
    fun search(
        @Header("Authorization") bearerToken: String,
        @Query("query", encoded = true) query: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false
    ): Call<SearchResponse>
}