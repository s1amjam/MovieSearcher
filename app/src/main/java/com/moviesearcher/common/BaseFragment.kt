package com.moviesearcher.common

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.moviesearcher.MovieSearcherFragmentDirections
import com.moviesearcher.R
import com.moviesearcher.api.Api
import com.moviesearcher.common.model.common.MediaId
import com.moviesearcher.favorite.common.model.MarkAsFavoriteRequest
import com.moviesearcher.list.CreateNewListDialog
import com.moviesearcher.list.lists.viewmodel.MyListsViewModel
import com.moviesearcher.list.model.Result
import com.moviesearcher.utils.EncryptedSharedPrefs
import com.moviesearcher.watchlist.common.model.WatchlistRequest

private const val TAG = "BaseFragment"

open class BaseFragment : Fragment() {
    lateinit var sessionId: String
    var accountId: Int = 0
    private lateinit var encryptedSharedPrefs: SharedPreferences
    private val myListsViewModel: MyListsViewModel by viewModels()
    private var isFavorite = true
    private var isWatchlist = true
    private lateinit var mediaInfo: MutableMap<String, Long>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        encryptedSharedPrefs = EncryptedSharedPrefs.sharedPrefs(requireContext())
        sessionId = encryptedSharedPrefs.getString("sessionId", "").toString()
        accountId = encryptedSharedPrefs.getInt("accountId", 0)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_movie_searcher_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(searchQuery: String): Boolean {
                    navigateToSearchResult(searchQuery)
                    return false
                }

                override fun onQueryTextChange(queryText: String): Boolean {
                    return false
                }
            })
        }
    }

    //TODO: different actions for different fragments
    private fun navigateToSearchResult(searchQuery: String) {
        val action =
            MovieSearcherFragmentDirections.actionMovieSearcherFragmentToSearchResultFragment(
                searchQuery
            )
        findNavController().navigate(action)
    }

    private fun showCreateNewListDialog() {
        val dialog = CreateNewListDialog()
        dialog.show(childFragmentManager, "CreateNewListDialogFragment")
    }

    private fun getMediaInfo(): MutableMap<String, Long> {
        val movieId = requireArguments().getLong("movie_id")
        val tvId = requireArguments().getLong("tv_id")
        val emptyMediaId: Long = 0
        val mediaInfo: MutableMap<String, Long> = mutableMapOf()

        if (movieId == emptyMediaId) {
            mediaInfo["tv"] = tvId
        } else {
            mediaInfo["movie"] = movieId
        }

        return mediaInfo
    }

    fun showAddToListMenu(v: View, @MenuRes menuRes: Int, resultList: MutableList<Result>) {
        val mediaId = getMediaInfo().values.first()

        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_create_new_list -> {
                    showCreateNewListDialog()
                }
            }
            true
        }

        resultList.forEach { it ->
            popup.menu.add(Menu.NONE, it.id!!.toInt(), Menu.NONE, it.name)
            val menuItem = popup.menu.findItem(it.id.toInt())

            myListsViewModel.checkItemStatus(it.id.toInt(), mediaId).observe(viewLifecycleOwner,
                { checkedItem ->
                    if (checkedItem.itemPresent == true) {
                        menuItem.isEnabled = false
                        menuItem.title = menuItem.title.toString() + " (added)"
                    } else {
                        menuItem.setOnMenuItemClickListener {
                            Api.addToList(it.itemId, MediaId(mediaId), sessionId)

                            true
                        }
                    }
                })
        }
        popup.show()
    }

    fun checkFavorites(button: Button) {
        if (sessionId.isNotEmpty()) {
            mediaInfo = getMediaInfo()
            val mediaId = mediaInfo.values.first()
            val mediaKey = mediaInfo.keys.first()
            val favoriteMovies = Api.getFavoriteMovies(accountId, sessionId)
            val favoriteTvs = Api.getFavoriteTvs(accountId, sessionId)

            if (mediaKey == "movie") {
                favoriteMovies.observe(viewLifecycleOwner, { favoriteItem ->
                    button.text = "Mark As Favorite"
                    favoriteItem.results!!.forEach {
                        if (it.id == mediaId) {
                            isFavorite = false
                            button.text = "Remove From Favorite"
                        }
                    }
                })
            } else {
                favoriteTvs.observe(viewLifecycleOwner, { favoriteItem ->
                    button.text = "Mark As Favorite"
                    favoriteItem.results!!.forEach {
                        if (it.id == mediaId) {
                            isFavorite = false
                            button.text = "Remove From Favorite"
                        }
                    }
                })
            }
        }
    }

    fun markAsFavorite(button: Button) {
        mediaInfo = getMediaInfo()

        val markAsFavorite = Api.markAsFavorite(
            accountId,
            sessionId,
            MarkAsFavoriteRequest(isFavorite, mediaInfo.values.first(), mediaInfo.keys.first())
        )

        markAsFavorite.observe(viewLifecycleOwner, {
            if (it.statusCode == 13 || it.statusCode == 1 || it.statusCode == 12) {
                isFavorite = true
                checkFavorites(button)
            }
        })
    }

    fun checkWatchlist(button: Button) {
        if (sessionId.isNotEmpty()) {
            mediaInfo = getMediaInfo()
            val mediaId = mediaInfo.values.first()
            val mediaKey = mediaInfo.keys.first()
            val moviesWatchlist = Api.getMovieWatchlist(accountId, sessionId)
            val tvsWatchlist = Api.getTvWatchlist(accountId, sessionId)

            if (mediaKey == "movie") {
                moviesWatchlist.observe(viewLifecycleOwner, { favoriteItem ->
                    button.text = "Add to Watchlist"
                    favoriteItem.results!!.forEach {
                        if (it.id == mediaId) {
                            isWatchlist = false
                            button.text = "Remove From Watchlist"
                        }
                    }
                })
            } else {
                tvsWatchlist.observe(viewLifecycleOwner, { favoriteItem ->
                    button.text = "Add to Watchlist"
                    favoriteItem.results!!.forEach {
                        if (it.id == mediaId) {
                            isWatchlist = false
                            button.text = "Remove From Watchlist"
                        }
                    }
                })
            }
        }
    }

    fun watchlist(button: Button) {
        mediaInfo = getMediaInfo()

        val markAsFavorite = Api.watchlist(
            accountId,
            sessionId,
            WatchlistRequest(isWatchlist, mediaInfo.values.first(), mediaInfo.keys.first())
        )

        markAsFavorite.observe(viewLifecycleOwner, {
            if (it.statusCode == 13 || it.statusCode == 1 || it.statusCode == 12) {
                isWatchlist = true
                checkWatchlist(button)
            }
        })
    }
}