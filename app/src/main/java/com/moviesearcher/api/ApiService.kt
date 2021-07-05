package com.moviesearcher.api

import com.moviesearcher.api.entity.MediaId
import com.moviesearcher.api.entity.account.AccountResponse
import com.moviesearcher.api.entity.auth.CreateSessionResponse
import com.moviesearcher.api.entity.auth.CreateTokenResponse
import com.moviesearcher.api.entity.auth.DeleteSessionResponse
import com.moviesearcher.api.entity.auth.RequestToken
import com.moviesearcher.api.entity.auth.SessionId
import com.moviesearcher.api.entity.favorites.FavoriteMovieResponse
import com.moviesearcher.api.entity.favorites.FavoriteTvResponse
import com.moviesearcher.api.entity.list.CheckItemStatusResponse
import com.moviesearcher.api.entity.list.ListResponse
import com.moviesearcher.api.entity.list.ListsResponse
import com.moviesearcher.api.entity.list.add.AddToListResponse
import com.moviesearcher.api.entity.movieinfo.MovieInfoResponse
import com.moviesearcher.api.entity.rated.movie.RatedMoviesResponse
import com.moviesearcher.api.entity.rated.tv.RatedTvsResponse
import com.moviesearcher.api.entity.rated.tvepisode.RatedTvEpisodesResponse
import com.moviesearcher.api.entity.search.SearchResponse
import com.moviesearcher.api.entity.trending.TrendingResponse
import com.moviesearcher.api.entity.tvinfo.TvInfoResponse
import com.moviesearcher.api.entity.watchlist.movie.MovieWatchlistResponse
import com.moviesearcher.api.entity.watchlist.tv.TvWatchlistResponse
import com.moviesearcher.utils.Constants
import com.moviesearcher.utils.Constants.ACCESS_TOKEN
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    companion object {
        fun create(): ApiService {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val apiKeyInterceptor = Interceptor { chain: Interceptor.Chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .addHeader("Authorization", ACCESS_TOKEN)
                        .build()
                )
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(apiKeyInterceptor)
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
        @Path("media_type") mediaType: String,
        @Path("time_window") timeWindow: String
    ): Call<TrendingResponse>

    @GET("movie/{movie_id}")
    fun movieInfo(@Path("movie_id") movieId: Int): Call<MovieInfoResponse>

    @GET("tv/{tv_id}")
    fun tvInfo(@Path("tv_id") tvId: Int): Call<TvInfoResponse>

    @GET("search/multi")
    fun search(
        @Query("query", encoded = true) query: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") includeAdult: Boolean = false
    ): Call<SearchResponse>

    @GET("authentication/token/new")
    fun newRequestToken(): Call<CreateTokenResponse>

    @POST("authentication/session/new")
    fun createSession(@Body requestToken: RequestToken): Call<CreateSessionResponse>

    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    fun deleteSession(@Body sessionId: SessionId): Call<DeleteSessionResponse>

    @GET("account")
    fun getAccount(@Query("session_id") sessionId: String): Call<AccountResponse>

    @GET("account/{account_id}/lists")
    fun getCreatedLists(
        @Path("account_id") accountId: Int?,
        @Query("session_id") sessionId: String?,
        @Query("page") page: Int = 1
    ): Call<ListsResponse>

    @GET("account/{account_id}/favorite/movies")
    fun getFavoriteMovies(
        @Path("account_id") accountId: Int?,
        @Query("session_id") sessionId: String?
    ): Call<FavoriteMovieResponse>

    @GET("account/{account_id}/favorite/tv")
    fun getFavoriteTvs(
        @Path("account_id") accountId: Int?,
        @Query("session_id") sessionId: String?
    ): Call<FavoriteTvResponse>

    @GET("account/{account_id}/rated/movies")
    fun getRatedMovies(
        @Path("account_id") accountId: Int?,
        @Query("session_id") sessionId: String?
    ): Call<RatedMoviesResponse>

    @GET("account/{account_id}/rated/tv")
    fun getRatedTvs(
        @Path("account_id") accountId: Int?,
        @Query("session_id") sessionId: String?
    ): Call<RatedTvsResponse>

    @GET("account/{account_id}/rated/tv/episodes")
    fun getRatedTvEpisodes(
        @Path("account_id") accountId: Int?,
        @Query("session_id") sessionId: String?
    ): Call<RatedTvEpisodesResponse>

    @GET("account/{account_id}/watchlist/movies")
    fun getMovieWatchlist(
        @Path("account_id") accountId: Int?,
        @Query("session_id") sessionId: String?
    ): Call<MovieWatchlistResponse>

    @GET("account/{account_id}/watchlist/tv")
    fun getTvWatchlist(
        @Path("account_id") accountId: Int?,
        @Query("session_id") sessionId: String?
    ): Call<TvWatchlistResponse>

    @POST("list/{list_id}/add_item")
    @Headers("Content-Type: application/json;charset=utf-8")
    fun addToList(
        @Path("list_id") listId: Int,
        @Query("session_id") sessionId: String?,
        @Body mediaId: MediaId
    ): Call<AddToListResponse>

    @GET("list/{list_id}")
    fun getListInfo(
        @Path("list_id") listId: Int
    ): Call<ListResponse>

    @GET("list/{list_id}/item_status")
    fun checkItemStatus(
        @Path("list_id") listId: Int,
        @Query("movie_id") movieId: Int
    ): Call<CheckItemStatusResponse>
}