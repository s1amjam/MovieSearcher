package com.moviesearcher

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.MediaId
import com.moviesearcher.utils.EncryptedSharedPrefs
import com.moviesearcher.viewmodel.MyListsViewModel

private const val TAG = "BaseFragment"

open class BaseFragment : Fragment() {
    private lateinit var sessionId: String
    private var accountId: Int? = null
    private lateinit var encryptedSharedPrefs: SharedPreferences
    private lateinit var myListsViewModel: MyListsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    private fun showCreateNewListDialog() {
        val dialog = CreateNewListDialog()
        dialog.show(childFragmentManager, "CreateNewListDialogFragment")
    }

    fun showAddToListMenu(v: View, @MenuRes menuRes: Int) {
        myListsViewModel = ViewModelProvider(this).get(MyListsViewModel::class.java)

        val movieId = requireArguments().getInt("movie_id")
        val tvId = requireArguments().getInt("tv_id")
        val mediaId = if (movieId == 0) tvId else movieId

        encryptedSharedPrefs = EncryptedSharedPrefs.sharedPrefs(requireContext())
        sessionId = encryptedSharedPrefs.getString("sessionId", "").toString()
        accountId = encryptedSharedPrefs.getString("accountId", "")?.toInt()

        myListsViewModel.getLists(accountId!!, sessionId, 1)

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

        myListsViewModel.myListsItemLiveData.observe(
            viewLifecycleOwner,
            { myListItems ->
                myListItems.results?.forEach { it ->
                    popup.menu.add(Menu.NONE, it.id!!.toInt(), Menu.NONE, it.name)
                    val menuItem = popup.menu.findItem(it.id.toInt())

                    myListsViewModel.checkItemStatus(it.id.toInt(), mediaId)

                    myListsViewModel.checkItemLiveData.observe(viewLifecycleOwner,
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
            })
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
}