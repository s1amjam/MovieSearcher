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
import com.moviesearcher.common.model.images.ImagesResponse
import com.moviesearcher.common.model.videos.VideosResponse
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

    fun getMovieInfo(movieId: Long): MutableLiveData<MovieInfoResponse> {
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

    fun getTvInfo(tvId: Long): MutableLiveData<TvInfoResponse> {
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

    fun getFavoriteMovies(
        accountId: Long?,
        sessionId: String?
    ): MutableLiveData<FavoriteMovieResponse> {
        val responseLiveData: MutableLiveData<FavoriteMovieResponse> = MutableLiveData()
        val resp = ApiService.create().getFavoriteMovies(accountId, sessionId)

        resp.enqueue(object : Callback<FavoriteMovieResponse> {
            override fun onResponse(
                call: Call<FavoriteMovieResponse>,
                response: Response<FavoriteMovieResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<FavoriteMovieResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun getFavoriteTvs(
        accountId: Long?,
        sessionId: String?
    ): MutableLiveData<FavoriteTvResponse> {
        val responseLiveData: MutableLiveData<FavoriteTvResponse> = MutableLiveData()
        val resp = ApiService.create().getFavoriteTvs(accountId, sessionId)

        resp.enqueue(object : Callback<FavoriteTvResponse> {
            override fun onResponse(
                call: Call<FavoriteTvResponse>,
                response: Response<FavoriteTvResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<FavoriteTvResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun getRatedMovies(
        accountId: Long?,
        sessionId: String?
    ): MutableLiveData<RatedMoviesResponse> {
        val responseLiveData: MutableLiveData<RatedMoviesResponse> = MutableLiveData()
        val resp = ApiService.create().getRatedMovies(accountId, sessionId)

        resp.enqueue(object : Callback<RatedMoviesResponse> {
            override fun onResponse(
                call: Call<RatedMoviesResponse>,
                response: Response<RatedMoviesResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<RatedMoviesResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun getRatedTvs(
        accountId: Long?,
        sessionId: String?
    ): MutableLiveData<RatedTvsResponse> {
        val responseLiveData: MutableLiveData<RatedTvsResponse> = MutableLiveData()
        val resp = ApiService.create().getRatedTvs(accountId, sessionId)

        resp.enqueue(object : Callback<RatedTvsResponse> {
            override fun onResponse(
                call: Call<RatedTvsResponse>,
                response: Response<RatedTvsResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<RatedTvsResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun getRatedTvEpisodes(
        accountId: Long?,
        sessionId: String?
    ): MutableLiveData<RatedTvEpisodesResponse> {
        val responseLiveData: MutableLiveData<RatedTvEpisodesResponse> = MutableLiveData()
        val resp = ApiService.create().getRatedTvEpisodes(accountId, sessionId)

        resp.enqueue(object : Callback<RatedTvEpisodesResponse> {
            override fun onResponse(
                call: Call<RatedTvEpisodesResponse>,
                response: Response<RatedTvEpisodesResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<RatedTvEpisodesResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun getMovieWatchlist(
        accountId: Long?,
        sessionId: String?
    ): MutableLiveData<MovieWatchlistResponse> {
        val responseLiveData: MutableLiveData<MovieWatchlistResponse> = MutableLiveData()
        val resp = ApiService.create().getMovieWatchlist(accountId, sessionId)

        resp.enqueue(object : Callback<MovieWatchlistResponse> {
            override fun onResponse(
                call: Call<MovieWatchlistResponse>,
                response: Response<MovieWatchlistResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<MovieWatchlistResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun getTvWatchlist(
        accountId: Long?,
        sessionId: String?
    ): MutableLiveData<TvWatchlistResponse> {
        val responseLiveData: MutableLiveData<TvWatchlistResponse> = MutableLiveData()
        val resp = ApiService.create().getTvWatchlist(accountId, sessionId)

        resp.enqueue(object : Callback<TvWatchlistResponse> {
            override fun onResponse(
                call: Call<TvWatchlistResponse>,
                response: Response<TvWatchlistResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<TvWatchlistResponse>,
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

    fun getListInfo(listId: Int): MutableLiveData<ListResponse> {
        val responseLiveData: MutableLiveData<ListResponse> = MutableLiveData()
        val resp = ApiService.create().getListInfo(listId)

        resp.enqueue(object : Callback<ListResponse> {
            override fun onResponse(
                call: Call<ListResponse>,
                response: Response<ListResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<ListResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun checkItemStatus(listId: Int, movieId: Long): MutableLiveData<CheckItemStatusResponse> {
        val responseLiveData: MutableLiveData<CheckItemStatusResponse> = MutableLiveData()
        val resp = ApiService.create().checkItemStatus(listId, movieId)

        resp.enqueue(object : Callback<CheckItemStatusResponse> {
            override fun onResponse(
                call: Call<CheckItemStatusResponse>,
                response: Response<CheckItemStatusResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<CheckItemStatusResponse>,
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

    fun getMovieCast(movieId: Long): MutableLiveData<MovieCastResponse> {
        val responseLiveData: MutableLiveData<MovieCastResponse> = MutableLiveData()
        val resp = ApiService.create().movieCast(movieId)

        resp.enqueue(object : Callback<MovieCastResponse> {
            override fun onResponse(
                call: Call<MovieCastResponse>,
                response: Response<MovieCastResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<MovieCastResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun getTvCast(tvId: Long): MutableLiveData<TvCastResponse> {
        val responseLiveData: MutableLiveData<TvCastResponse> = MutableLiveData()
        val resp = ApiService.create().tvCast(tvId)

        resp.enqueue(object : Callback<TvCastResponse> {
            override fun onResponse(
                call: Call<TvCastResponse>,
                response: Response<TvCastResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<TvCastResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun getRecommendations(movieId: Long): MutableLiveData<FavoriteMovieResponse> {
        val responseLiveData: MutableLiveData<FavoriteMovieResponse> = MutableLiveData()
        val resp = ApiService.create().recommendations(movieId)

        resp.enqueue(object : Callback<FavoriteMovieResponse> {
            override fun onResponse(
                call: Call<FavoriteMovieResponse>,
                response: Response<FavoriteMovieResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<FavoriteMovieResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun getTvRecommendations(tvId: Long): MutableLiveData<FavoriteTvResponse> {
        val responseLiveData: MutableLiveData<FavoriteTvResponse> = MutableLiveData()
        val resp = ApiService.create().tvRecommendations(tvId)

        resp.enqueue(object : Callback<FavoriteTvResponse> {
            override fun onResponse(
                call: Call<FavoriteTvResponse>,
                response: Response<FavoriteTvResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<FavoriteTvResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun videos(movieId: Long): MutableLiveData<VideosResponse> {
        val responseLiveData: MutableLiveData<VideosResponse> = MutableLiveData()
        val resp = ApiService.create().videos(movieId)

        resp.enqueue(object : Callback<VideosResponse> {
            override fun onResponse(
                call: Call<VideosResponse>,
                response: Response<VideosResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<VideosResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun tvVideos(tvId: Long): MutableLiveData<VideosResponse> {
        val responseLiveData: MutableLiveData<VideosResponse> = MutableLiveData()
        val resp = ApiService.create().tvVideos(tvId)

        resp.enqueue(object : Callback<VideosResponse> {
            override fun onResponse(
                call: Call<VideosResponse>,
                response: Response<VideosResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<VideosResponse>,
                t: Throwable
            ) {

            }
        }
        )
        return responseLiveData
    }

    fun images(movieId: Long): MutableLiveData<ImagesResponse> {
        val responseLiveData: MutableLiveData<ImagesResponse> = MutableLiveData()
        val resp = ApiService.create().images(movieId)

        resp.enqueue(object : Callback<ImagesResponse> {
            override fun onResponse(
                call: Call<ImagesResponse>,
                response: Response<ImagesResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<ImagesResponse>,
                t: Throwable
            ) {
            }
        }
        )
        return responseLiveData
    }

    fun tvImages(tvId: Long): MutableLiveData<ImagesResponse> {
        val responseLiveData: MutableLiveData<ImagesResponse> = MutableLiveData()
        val resp = ApiService.create().tvImages(tvId)

        resp.enqueue(object : Callback<ImagesResponse> {
            override fun onResponse(
                call: Call<ImagesResponse>,
                response: Response<ImagesResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<ImagesResponse>,
                t: Throwable
            ) {
            }
        }
        )
        return responseLiveData
    }

    fun person(personId: Long): MutableLiveData<PersonResponse> {
        val responseLiveData: MutableLiveData<PersonResponse> = MutableLiveData()
        val resp = ApiService.create().person(personId)

        resp.enqueue(object : Callback<PersonResponse> {
            override fun onResponse(
                call: Call<PersonResponse>,
                response: Response<PersonResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<PersonResponse>,
                t: Throwable
            ) {
            }
        }
        )
        return responseLiveData
    }

    fun personImages(personId: Long): MutableLiveData<PersonImagesResponse> {
        val responseLiveData: MutableLiveData<PersonImagesResponse> = MutableLiveData()
        val resp = ApiService.create().personImages(personId)

        resp.enqueue(object : Callback<PersonImagesResponse> {
            override fun onResponse(
                call: Call<PersonImagesResponse>,
                response: Response<PersonImagesResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<PersonImagesResponse>,
                t: Throwable
            ) {
            }
        }
        )
        return responseLiveData
    }

    fun personCombinedCredits(personId: Long): MutableLiveData<CombinedCreditsResponse> {
        val responseLiveData: MutableLiveData<CombinedCreditsResponse> = MutableLiveData()
        val resp = ApiService.create().personCombinedCredits(personId)

        resp.enqueue(object : Callback<CombinedCreditsResponse> {
            override fun onResponse(
                call: Call<CombinedCreditsResponse>,
                response: Response<CombinedCreditsResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<CombinedCreditsResponse>,
                t: Throwable
            ) {
            }
        }
        )
        return responseLiveData
    }

    fun getTvEpisode(
        tvId: Long,
        seasonNumber: String,
        episodeNumber: Int
    ): MutableLiveData<TvEpisodeResponse> {
        val responseLiveData: MutableLiveData<TvEpisodeResponse> = MutableLiveData()
        val resp = ApiService.create().getTvEpisode(tvId, seasonNumber, episodeNumber)

        resp.enqueue(object : Callback<TvEpisodeResponse> {
            override fun onResponse(
                call: Call<TvEpisodeResponse>,
                response: Response<TvEpisodeResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<TvEpisodeResponse>,
                t: Throwable
            ) {
            }
        }
        )
        return responseLiveData
    }

    fun getTvSeason(
        tvId: Long?,
        seasonNumber: String?,
    ): MutableLiveData<TvSeasonResponse> {
        val responseLiveData: MutableLiveData<TvSeasonResponse> = MutableLiveData()
        val resp = ApiService.create().getTvSeason(tvId, seasonNumber)

        resp.enqueue(object : Callback<TvSeasonResponse> {
            override fun onResponse(
                call: Call<TvSeasonResponse>,
                response: Response<TvSeasonResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<TvSeasonResponse>,
                t: Throwable
            ) {
            }
        }
        )
        return responseLiveData
    }

    fun getTvEpisodeImages(
        tvId: Long,
        seasonNumber: String,
        episodeNumber: Int
    ): MutableLiveData<EpisodeImageResponse> {
        val responseLiveData: MutableLiveData<EpisodeImageResponse> = MutableLiveData()
        val resp = ApiService.create().getTvEpisodeImages(tvId, seasonNumber, episodeNumber)

        resp.enqueue(object : Callback<EpisodeImageResponse> {
            override fun onResponse(
                call: Call<EpisodeImageResponse>,
                response: Response<EpisodeImageResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<EpisodeImageResponse>,
                t: Throwable
            ) {
            }
        }
        )
        return responseLiveData
    }

    fun getTvEpisodeVideos(
        tvId: Long,
        seasonNumber: String,
        episodeNumber: Int
    ): MutableLiveData<VideosResponse> {
        val responseLiveData: MutableLiveData<VideosResponse> = MutableLiveData()
        val resp = ApiService.create().getTvEpisodeVideos(tvId, seasonNumber, episodeNumber)

        resp.enqueue(object : Callback<VideosResponse> {
            override fun onResponse(
                call: Call<VideosResponse>,
                response: Response<VideosResponse>
            ) {
                responseLiveData.value = response.body()
            }

            override fun onFailure(
                call: Call<VideosResponse>,
                t: Throwable
            ) {
            }
        }
        )
        return responseLiveData
    }
}