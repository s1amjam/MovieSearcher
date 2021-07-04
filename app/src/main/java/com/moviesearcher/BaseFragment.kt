package com.moviesearcher

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import com.moviesearcher.utils.EncryptedSharedPrefs
import com.moviesearcher.viewmodel.MyListsViewModel

private const val TAG = "BaseFragment"

open class BaseFragment : Fragment() {
    private lateinit var menuButtonAddToList: Button
    private lateinit var sessionId: String
    private var accountId: Int? = null
    private lateinit var encryptedSharedPrefs: SharedPreferences
    private lateinit var myListsViewModel: MyListsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    fun showMenu(v: View, @MenuRes menuRes: Int) {
        myListsViewModel = ViewModelProvider(this).get(MyListsViewModel::class.java)

        encryptedSharedPrefs = EncryptedSharedPrefs.sharedPrefs(requireContext())
        sessionId = encryptedSharedPrefs.getString("sessionId", "").toString()
        accountId = encryptedSharedPrefs.getString("accountId", "")?.toInt()

        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        myListsViewModel.getLists(accountId!!, sessionId, 1)

        val movieId = requireArguments().getInt("movie_id")
        val tvId = requireArguments().getInt("tv_id")

        Log.d(TAG, "showMenu: $movieId")
        Log.d(TAG, "showMenu: $tvId")

        myListsViewModel.myListsItemLiveData.observe(
            viewLifecycleOwner,
            { myListItems ->
                myListItems.results?.forEach { it ->
                    popup.menu.add(Menu.NONE, it.id!!.toInt(), Menu.NONE, it.name)
                        .setOnMenuItemClickListener {


                            true
                        }
                }
                popup.show()
            })

        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_create_new_list -> {

                }

            }
            true
        }
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