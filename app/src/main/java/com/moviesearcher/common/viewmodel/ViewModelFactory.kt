package com.moviesearcher.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moviesearcher.favorite.FavoriteViewModel
import com.moviesearcher.list.ListViewModel
import com.moviesearcher.list.lists.ListsViewModel
import com.moviesearcher.movie.MovieViewModel
import com.moviesearcher.person.PersonViewModel
import com.moviesearcher.rated.RatedViewModel
import com.moviesearcher.search.SearchViewModel
import com.moviesearcher.tv.TvViewModel
import com.moviesearcher.tv.episode.TvEpisodeViewModel
import com.moviesearcher.tv.seasons.TvSeasonViewModel
import com.moviesearcher.watchlist.common.viewmodel.WatchlistViewModel

class ViewModelFactory(
    private val sessionId: String? = null,
    private val accountId: Long? = null,
    private val movieId: Long? = null,
    private val tvId: Long? = null,
    private val seasonNumber: String? = null,
    private val episodeNumber: Int? = null,
    private val personId: Long? = null,
    private val listId: Int? = null,
    private val page: Int? = null,
    private val isFavorite: Boolean? = null,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WatchlistViewModel::class.java)) {
            return WatchlistViewModel(accountId!!, sessionId!!) as T
        }
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return MovieViewModel(movieId!!) as T
        }
        if (modelClass.isAssignableFrom(TvViewModel::class.java)) {
            return TvViewModel(tvId!!) as T
        }
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(sessionId!!, accountId!!, isFavorite!!) as T
        }
        if (modelClass.isAssignableFrom(TvSeasonViewModel::class.java)) {
            return TvSeasonViewModel(tvId, seasonNumber) as T
        }
        if (modelClass.isAssignableFrom(TvEpisodeViewModel::class.java)) {
            return TvEpisodeViewModel(tvId!!, seasonNumber!!, episodeNumber!!) as T
        }
        if (modelClass.isAssignableFrom(PersonViewModel::class.java)) {
            return PersonViewModel(personId!!) as T
        }
        if (modelClass.isAssignableFrom(RatedViewModel::class.java)) {
            return RatedViewModel(sessionId!!, accountId!!) as T
        }
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel() as T
        }
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(listId = listId, movieId = movieId) as T
        }
        if (modelClass.isAssignableFrom(ListsViewModel::class.java)) {
            return ListsViewModel(accountId!!, sessionId!!, page!!) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}