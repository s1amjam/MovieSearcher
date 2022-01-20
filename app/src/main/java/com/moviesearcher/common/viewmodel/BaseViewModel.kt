package com.moviesearcher.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviesearcher.api.Api
import com.moviesearcher.api.ApiService
import com.moviesearcher.common.model.images.ImagesResponse
import com.moviesearcher.common.model.videos.VideosResponse
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.favorite.movie.model.FavoriteMovieResponse
import com.moviesearcher.favorite.tv.model.FavoriteTvResponse
import com.moviesearcher.list.lists.model.ListsResponse
import com.moviesearcher.list.model.CheckItemStatusResponse
import com.moviesearcher.list.model.ListResponse
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
import com.moviesearcher.watchlist.movie.model.MovieWatchlistResponse
import com.moviesearcher.watchlist.tv.model.TvWatchlistResponse
import kotlinx.coroutines.launch

class BaseViewModel : ViewModel() {
    private val searchItem: LiveData<SearchResponse>
    private var mutableQuery: MutableLiveData<String> = MutableLiveData()
    private lateinit var movieCast: LiveData<MovieCastResponse>
    private lateinit var tvCast: LiveData<TvCastResponse>
    private lateinit var movieImages: LiveData<ImagesResponse>
    private lateinit var tvImages: LiveData<ImagesResponse>
    private lateinit var recommendations: LiveData<FavoriteMovieResponse>
    private lateinit var tvRecommendations: LiveData<FavoriteTvResponse>
    private lateinit var video: LiveData<VideosResponse>
    private lateinit var tvVideo: LiveData<VideosResponse>
    private lateinit var favoriteMovies: LiveData<FavoriteMovieResponse>
    private lateinit var favoriteTvs: LiveData<FavoriteTvResponse>
    private lateinit var myLists: LiveData<ListsResponse>
    private lateinit var checkedItem: LiveData<CheckItemStatusResponse>
    private lateinit var myList: LiveData<ListResponse>
    private lateinit var movieInfo: LiveData<MovieInfoResponse>
    private val trendingMovies = MutableLiveData<Resource<TrendingResponse>>()
    private val trendingTvs = MutableLiveData<Resource<TrendingResponse>>()
    private lateinit var person: LiveData<PersonResponse>
    private lateinit var personCombinedCredits: LiveData<CombinedCreditsResponse>
    private lateinit var personImages: LiveData<PersonImagesResponse>
    private lateinit var ratedMovies: LiveData<RatedMoviesResponse>
    private lateinit var ratedTvs: LiveData<RatedTvsResponse>
    private lateinit var ratedTvEpisodes: LiveData<RatedTvEpisodesResponse>
    private lateinit var tvEpisodeImages: LiveData<EpisodeImageResponse>
    private lateinit var episodeVideos: LiveData<VideosResponse>
    private lateinit var tvEpisode: LiveData<TvEpisodeResponse>
    private lateinit var tvSeason: LiveData<TvSeasonResponse>
    private lateinit var tvInfo: LiveData<TvInfoResponse>
    private lateinit var movieWatchlist: LiveData<MovieWatchlistResponse>
    private lateinit var tvWatchlist: LiveData<TvWatchlistResponse>

    init {
        searchItem = Transformations.switchMap(mutableQuery) { query ->
            Api.search(query)
        }

        fetchTrendingMovies()
        fetchTrendingTvs()
    }

    fun getTvWatchlist(accountId: Long, sessionId: String): LiveData<TvWatchlistResponse> {
        tvWatchlist = Api.getTvWatchlist(accountId, sessionId)

        return tvWatchlist
    }

    private fun fetchTrendingMovies() {
        viewModelScope.launch {
            trendingMovies.postValue(Resource.loading(null))
            try {
                val trendingMoviesFromApi = ApiService.create().getTrending("movie", "day")
                trendingMovies.postValue(Resource.success(trendingMoviesFromApi))
            } catch (e: Exception) {
                trendingMovies.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getTrendingMovies(): LiveData<Resource<TrendingResponse>> {
        return trendingMovies
    }

    private fun fetchTrendingTvs() {
        viewModelScope.launch {
            trendingTvs.postValue(Resource.loading(null))
            try {
                val trendingTvsFromApi = ApiService.create().getTrending("tv", "day")
                trendingTvs.postValue(Resource.success(trendingTvsFromApi))
            } catch (e: Exception) {
                trendingTvs.postValue(Resource.error(e.toString(), null))
            }
        }
    }

    fun getTrendingTvs(): LiveData<Resource<TrendingResponse>> {
        return trendingTvs
    }

    fun getTvWatchlistIds(): MutableList<Long> {
        val tvWatchlistIds: MutableList<Long> = mutableListOf()
        tvWatchlist.value?.results?.forEach { it -> tvWatchlistIds.add(it.id!!.toLong()) }

        return tvWatchlistIds
    }

    fun getMovieWatchlist(accountId: Long, sessionId: String): LiveData<MovieWatchlistResponse> {
        movieWatchlist = Api.getMovieWatchlist(accountId, sessionId)

        return movieWatchlist
    }

    fun getMovieWatchlistIds(): MutableList<Long> {
        val movieWatchlistIds: MutableList<Long> = mutableListOf()
        movieWatchlist.value?.results?.forEach { it -> movieWatchlistIds.add(it.id!!.toLong()) }

        return movieWatchlistIds
    }

    fun getTvInfoById(tvId: Long): LiveData<TvInfoResponse> {
        tvInfo = Api.getTvInfo(tvId)

        return tvInfo
    }

    fun getTvSeason(
        tvId: Long?,
        seasonNumber: String?,
    ): LiveData<TvSeasonResponse> {
        tvSeason = Api.getTvSeason(tvId, seasonNumber)

        return tvSeason
    }

    fun getTvEpisode(
        tvId: Long,
        seasonNumber: String,
        episodeNumber: Int
    ): LiveData<TvEpisodeResponse> {
        tvEpisode = Api.getTvEpisode(tvId, seasonNumber, episodeNumber)

        return tvEpisode
    }

    fun getTvEpisodeVideos(
        tvId: Long,
        seasonNumber: String,
        episodeNumber: Int
    ): LiveData<VideosResponse> {
        episodeVideos = Api.getTvEpisodeVideos(tvId, seasonNumber, episodeNumber)

        return episodeVideos
    }

    fun getTvEpisodeImages(
        tvId: Long,
        seasonNumber: String,
        episodeNumber: Int
    ): LiveData<EpisodeImageResponse> {
        tvEpisodeImages = Api.getTvEpisodeImages(tvId, seasonNumber, episodeNumber)

        return tvEpisodeImages
    }

    fun queryForSearch(query: String): LiveData<SearchResponse> {
        mutableQuery.value = query

        return searchItem
    }

    fun getRatedTvEpisodes(accountId: Long, sessionId: String): LiveData<RatedTvEpisodesResponse> {
        ratedTvEpisodes = Api.getRatedTvEpisodes(accountId, sessionId)

        return ratedTvEpisodes
    }

    fun getRatedTvs(accountId: Long, sessionId: String): LiveData<RatedTvsResponse> {
        ratedTvs = Api.getRatedTvs(accountId, sessionId)

        return ratedTvs
    }

    fun getTvCastById(tvId: Long): LiveData<TvCastResponse> {
        tvCast = Api.getTvCast(tvId)

        return tvCast
    }

    fun getMovieCastById(movieId: Long): LiveData<MovieCastResponse> {
        movieCast = Api.getMovieCast(movieId)

        return movieCast
    }

    fun getImagesByMovieId(movieId: Long): LiveData<ImagesResponse> {
        movieImages = Api.images(movieId)

        return movieImages
    }

    fun getImagesByTvId(tvId: Long): LiveData<ImagesResponse> {
        tvImages = Api.tvImages(tvId)

        return tvImages
    }

    fun getRecommendationsByMovieId(movieId: Long): LiveData<FavoriteMovieResponse> {
        recommendations = Api.getRecommendations(movieId)

        return recommendations
    }

    fun getRecommendationsByTvId(tvId: Long): LiveData<FavoriteTvResponse> {
        tvRecommendations = Api.getTvRecommendations(tvId)

        return tvRecommendations
    }

    fun getVideosByTvId(tvId: Long): LiveData<VideosResponse> {
        tvVideo = Api.tvVideos(tvId)

        return tvVideo
    }

    fun getVideosByMovieId(movieId: Long): LiveData<VideosResponse> {
        video = Api.videos(movieId)

        return video
    }

    fun getFavoriteMovies(accountId: Long, sessionId: String): LiveData<FavoriteMovieResponse> {
        favoriteMovies = Api.getFavoriteMovies(accountId, sessionId)

        return favoriteMovies
    }

    fun getFavoriteTvs(accountId: Long, sessionId: String): LiveData<FavoriteTvResponse> {
        favoriteTvs = Api.getFavoriteTvs(accountId, sessionId)

        return favoriteTvs
    }

    fun getLists(accountId: Long, sessionId: String, page: Int): LiveData<ListsResponse> {
        myLists = Api.getLists(accountId, sessionId, page)

        return myLists
    }

    fun checkItemStatus(listId: Int, movieId: Long): LiveData<CheckItemStatusResponse> {
        checkedItem = Api.checkItemStatus(listId, movieId)

        return checkedItem
    }

    fun getList(listId: Int): LiveData<ListResponse> {
        myList = Api.getListInfo(listId)

        return myList
    }

    fun getMovieInfoById(movieId: Long): LiveData<MovieInfoResponse> {
        movieInfo = Api.getMovieInfo(movieId)

        return movieInfo
    }

    fun getPersonById(personId: Long): LiveData<PersonResponse> {
        person = Api.person(personId)

        return person
    }

    fun getCombinedCreditsByPersonId(personId: Long): LiveData<CombinedCreditsResponse> {
        personCombinedCredits = Api.personCombinedCredits(personId)

        return personCombinedCredits
    }

    fun getImagesByPersonId(personId: Long): LiveData<PersonImagesResponse> {
        personImages = Api.personImages(personId)

        return personImages
    }

    fun getRatedMovies(accountId: Long, sessionId: String): LiveData<RatedMoviesResponse> {
        ratedMovies = Api.getRatedMovies(accountId, sessionId)

        return ratedMovies
    }
}