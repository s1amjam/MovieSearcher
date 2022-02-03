package com.moviesearcher.api

import com.moviesearcher.common.model.account.AccountResponse
import com.moviesearcher.common.model.auth.CreateSessionResponse
import com.moviesearcher.common.model.auth.CreateTokenResponse
import com.moviesearcher.common.model.auth.DeleteSessionResponse
import com.moviesearcher.common.model.auth.RequestToken
import com.moviesearcher.common.model.auth.SessionId
import com.moviesearcher.common.model.common.MediaId
import com.moviesearcher.common.model.common.ResponseWithCodeAndMessage
import com.moviesearcher.common.model.images.ImagesResponse
import com.moviesearcher.common.model.videos.VideosResponse
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.common.utils.Constants.ACCESS_TOKEN
import com.moviesearcher.favorite.common.model.MarkAsFavoriteRequest
import com.moviesearcher.favorite.movie.model.FavoriteMovieResponse
import com.moviesearcher.favorite.tv.model.FavoriteTvResponse
import com.moviesearcher.list.lists.model.ListsResponse
import com.moviesearcher.list.model.CheckItemStatusResponse
import com.moviesearcher.list.model.CreateNewList
import com.moviesearcher.list.model.CreateNewListResponse
import com.moviesearcher.list.model.ListResponse
import com.moviesearcher.list.model.add.AddToListResponse
import com.moviesearcher.movie.model.MovieInfoResponse
import com.moviesearcher.movie.model.TrendingResponse
import com.moviesearcher.movie.model.cast.MovieCastResponse
import com.moviesearcher.person.model.combinedcredits.CombinedCreditsResponse
import com.moviesearcher.person.model.images.PersonImagesResponse
import com.moviesearcher.person.model.person.PersonResponse
import com.moviesearcher.rated.movie.model.RatedMoviesResponse
import com.moviesearcher.rated.tv.model.RatedTvsResponse
import com.moviesearcher.rated.tvepisode.model.RatedTvEpisodesResponse
import com.moviesearcher.search.model.SearchResponse
import com.moviesearcher.tv.episode.model.TvEpisodeResponse
import com.moviesearcher.tv.episode.model.image.EpisodeImageResponse
import com.moviesearcher.tv.model.TvInfoResponse
import com.moviesearcher.tv.model.cast.TvCastResponse
import com.moviesearcher.tv.seasons.model.TvSeasonResponse
import com.moviesearcher.watchlist.common.model.WatchlistRequest
import com.moviesearcher.watchlist.movie.model.MovieWatchlistResponse
import com.moviesearcher.watchlist.tv.model.TvWatchlistResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
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
    suspend fun getTrending(
        @Path("media_type") mediaType: String,
        @Path("time_window") timeWindow: String
    ): TrendingResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("region") region: String = "RU"
    ): TrendingResponse

    @GET("movie/{movie_id}")
    suspend fun movieInfo(@Path("movie_id") movieId: Long): MovieInfoResponse

    @GET("tv/{tv_id}")
    suspend fun tvInfo(@Path("tv_id") tvId: Long): TvInfoResponse

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
        @Path("account_id") accountId: Long?,
        @Query("session_id") sessionId: String?,
        @Query("page") page: Int = 1
    ): Call<ListsResponse>

    @GET("account/{account_id}/favorite/movies")
    fun getFavoriteMovies(
        @Path("account_id") accountId: Long?,
        @Query("session_id") sessionId: String?
    ): Call<FavoriteMovieResponse>

    @GET("account/{account_id}/favorite/tv")
    fun getFavoriteTvs(
        @Path("account_id") accountId: Long?,
        @Query("session_id") sessionId: String?
    ): Call<FavoriteTvResponse>

    @GET("account/{account_id}/rated/movies")
    fun getRatedMovies(
        @Path("account_id") accountId: Long?,
        @Query("session_id") sessionId: String?
    ): Call<RatedMoviesResponse>

    @GET("account/{account_id}/rated/tv")
    fun getRatedTvs(
        @Path("account_id") accountId: Long?,
        @Query("session_id") sessionId: String?
    ): Call<RatedTvsResponse>

    @GET("account/{account_id}/rated/tv/episodes")
    fun getRatedTvEpisodes(
        @Path("account_id") accountId: Long?,
        @Query("session_id") sessionId: String?
    ): Call<RatedTvEpisodesResponse>

    @GET("account/{account_id}/watchlist/movies")
    suspend fun getMovieWatchlist(
        @Path("account_id") accountId: Long?,
        @Query("session_id") sessionId: String?
    ): MovieWatchlistResponse

    @GET("account/{account_id}/watchlist/tv")
    suspend fun getTvWatchlist(
        @Path("account_id") accountId: Long?,
        @Query("session_id") sessionId: String?
    ): TvWatchlistResponse

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
        @Query("movie_id") movieId: Long
    ): Call<CheckItemStatusResponse>

    @POST("list")
    @Headers("Content-Type: application/json;charset=utf-8")
    fun createNewList(
        @Query("session_id") sessionId: String?,
        @Body createNewList: CreateNewList
    ): Call<CreateNewListResponse>

    @POST("list/{list_id}/remove_item")
    @Headers("Content-Type: application/json;charset=utf-8")
    fun removeFromList(
        @Path("list_id") listId: Int,
        @Query("session_id") sessionId: String?,
        @Body mediaId: MediaId
    ): Call<ResponseWithCodeAndMessage>

    @DELETE("list/{list_id}")
    fun deleteList(
        @Path("list_id") listId: Int,
        @Query("session_id") sessionId: String
    ): Call<ResponseWithCodeAndMessage>

    @POST("account/{account_id}/favorite")
    @Headers("Content-Type: application/json;charset=utf-8")
    fun markAsFavorite(
        @Path("account_id") accountId: Long,
        @Query("session_id") sessionId: String?,
        @Body markAsFavorite: MarkAsFavoriteRequest
    ): Call<ResponseWithCodeAndMessage>

    @POST("account/{account_id}/watchlist")
    @Headers("Content-Type: application/json;charset=utf-8")
    fun watchlist(
        @Path("account_id") accountId: Long,
        @Query("session_id") sessionId: String?,
        @Body watchlist: WatchlistRequest
    ): Call<ResponseWithCodeAndMessage>

    @GET("movie/{movie_id}/credits")
    suspend fun movieCast(@Path("movie_id") movieId: Long): MovieCastResponse

    @GET("tv/{tv_id}/aggregate_credits")
    suspend fun tvCast(@Path("tv_id") tvId: Long): TvCastResponse

    @GET("movie/{movie_id}/recommendations")
    suspend fun recommendations(@Path("movie_id") movieId: Long): FavoriteMovieResponse

    @GET("tv/{tv_id}/recommendations")
    suspend fun tvRecommendations(@Path("tv_id") tvId: Long): FavoriteTvResponse

    @GET("movie/{movie_id}/videos")
    suspend fun videos(@Path("movie_id") movieId: Long): VideosResponse

    @GET("tv/{tv_id}/videos")
    suspend fun tvVideos(@Path("tv_id") tvId: Long): VideosResponse

    @GET("movie/{movie_id}/images")
    suspend fun images(@Path("movie_id") movieId: Long): ImagesResponse

    @GET("tv/{tv_id}/images")
    suspend fun tvImages(@Path("tv_id") tvId: Long): ImagesResponse

    @GET("person/{person_id}")
    fun person(@Path("person_id") personId: Long): Call<PersonResponse>

    @GET("person/{person_id}/combined_credits")
    fun personCombinedCredits(@Path("person_id") personId: Long): Call<CombinedCreditsResponse>

    @GET("person/{person_id}/images")
    fun personImages(@Path("person_id") personId: Long): Call<PersonImagesResponse>

    @GET("tv/{tv_id}/season/{season_number}/episode/{episode_number}")
    fun getTvEpisode(
        @Path("tv_id") tvId: Long,
        @Path("season_number") seasonNumber: String,
        @Path("episode_number") episodeNumber: Int,
    ): Call<TvEpisodeResponse>

    @GET("tv/{tv_id}/season/{season_number}/episode/{episode_number}/images")
    fun getTvEpisodeImages(
        @Path("tv_id") tvId: Long,
        @Path("season_number") seasonNumber: String,
        @Path("episode_number") episodeNumber: Int,
    ): Call<EpisodeImageResponse>

    @GET("tv/{tv_id}/season/{season_number}/episode/{episode_number}/videos")
    fun getTvEpisodeVideos(
        @Path("tv_id") tvId: Long,
        @Path("season_number") seasonNumber: String,
        @Path("episode_number") episodeNumber: Int,
    ): Call<VideosResponse>

    @GET("tv/{tv_id}/season/{season_number}")
    fun getTvSeason(
        @Path("tv_id") tvId: Long?,
        @Path("season_number") seasonNumber: String?,
    ): Call<TvSeasonResponse>
}