package com.moviesearcher

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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.common.MediaId
import com.moviesearcher.api.entity.favorites.MarkAsFavoriteRequest
import com.moviesearcher.api.entity.list.Result
import com.moviesearcher.utils.EncryptedSharedPrefs
import com.moviesearcher.viewmodel.MyListsViewModel

private const val TAG = "BaseFragment"

open class BaseFragment : Fragment() {
    lateinit var sessionId: String
    var accountId: Int = 0
    private lateinit var encryptedSharedPrefs: SharedPreferences
    private lateinit var myListsViewModel: MyListsViewModel
    private var isFavorite = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        myListsViewModel = ViewModelProvider(this).get(MyListsViewModel::class.java)
        encryptedSharedPrefs = EncryptedSharedPrefs.sharedPrefs(requireContext())
        sessionId = encryptedSharedPrefs.getString("sessionId", "").toString()
        accountId = encryptedSharedPrefs.getString("accountId", "")!!.toInt()
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

    fun getLists(): MutableList<Result> {
        val resultList = mutableListOf<Result>()

        if (sessionId != "") {
            myListsViewModel.getLists(accountId, sessionId, 1)

            myListsViewModel.myListsItemLiveData.observe(
                viewLifecycleOwner,
                { myListItems ->
                    myListItems.results?.forEach { it ->
                        resultList.add(it)
                    }
                })
        }

        return resultList
    }

    private fun movieOrTv(): MutableMap<String, Long> {
        val movieId = requireArguments().getLong("movie_id")
        val tvId = requireArguments().getLong("tv_id")
        val emptyMediaId: Long = 0
        val mediaMap: MutableMap<String, Long> = mutableMapOf()

        if (movieId == emptyMediaId) {
            mediaMap["tv"] = tvId
        } else {
            mediaMap["movie"] = movieId
        }

        return mediaMap
    }

    fun showAddToListMenu(v: View, @MenuRes menuRes: Int, resultList: MutableList<Result>) {
        val mediaId = movieOrTv()

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

            myListsViewModel.checkItemStatus(it.id.toInt(), mediaId.values.first())

            myListsViewModel.checkItemLiveData.observe(viewLifecycleOwner,
                { checkedItem ->
                    if (checkedItem.itemPresent == true) {
                        menuItem.isEnabled = false
                        menuItem.title = menuItem.title.toString() + " (added)"
                    } else {
                        menuItem.setOnMenuItemClickListener {
                            Api.addToList(it.itemId, MediaId(mediaId.values.first()), sessionId)

                            true
                        }
                    }
                })
        }
        popup.show()
    }

    fun checkFavorites(button: Button) {
        val mediaId = movieOrTv()

        if (mediaId.keys.first() == "movie") {
            val favorites = Api.getFavoriteMovies(accountId, sessionId)

            favorites.observe(viewLifecycleOwner, { favorite ->
                button.text = "Mark As Favorite"
                favorite.results!!.forEach {
                    if (it.id == mediaId.values.first()) {
                        isFavorite = false
                        button.text = "Remove From Favorite"
                    }
                }
            })
        } else {
            val favorites = Api.getFavoriteTvs(accountId, sessionId)

            favorites.observe(viewLifecycleOwner, { favorite ->
                button.text = "Mark As Favorite"
                favorite.results!!.forEach {
                    if (it.id == mediaId.values.first()) {
                        isFavorite = false
                        button.text = "Remove From Favorite"
                    }
                }
            })
        }
    }

    fun markAsFavorite(button: Button) {
        val mediaId = movieOrTv()

        val markAsFavorite = Api.markAsFavorite(
            accountId,
            sessionId,
            MarkAsFavoriteRequest(isFavorite, mediaId.values.first(), mediaId.keys.first())
        )

        markAsFavorite.observe(viewLifecycleOwner, {
            if (it.statusCode == 13 || it.statusCode == 1 || it.statusCode == 12) {
                isFavorite = true
                checkFavorites(button)
            }
        })
    }
}