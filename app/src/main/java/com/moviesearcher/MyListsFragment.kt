package com.moviesearcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.adapters.MyListAdapter
import com.moviesearcher.utils.EncryptedSharedPrefs
import com.moviesearcher.viewmodel.MyListsViewModel

private const val TAG = "MyListsFragment"

class MyListsFragment : Fragment() {
    private lateinit var myListsRecyclerView: RecyclerView
    private lateinit var myListsViewModel: MyListsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    //TODO: remove to a separate class
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_lists, container, false)

        myListsRecyclerView = view.findViewById(R.id.fragment_my_list_recycler_view)
        myListsRecyclerView.layoutManager = LinearLayoutManager(context)
        myListsViewModel = ViewModelProvider(this).get(MyListsViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val encryptedSharedPrefs = EncryptedSharedPrefs.sharedPrefs(requireContext())
        val accountId: Int = encryptedSharedPrefs.getString("id", null)!!.toInt()
        val sessionId: String = encryptedSharedPrefs.getString("sessionId", null)!!

        myListsViewModel.getLists(accountId, sessionId, 1)

        myListsViewModel.myListsItemLiveData.observe(
            viewLifecycleOwner,
            { myListItems ->
                myListsRecyclerView.adapter = MyListAdapter(myListItems, findNavController())
            })
    }

    private fun navigateToSearchResult(searchQuery: String) {
        val action =
            TvInfoFragmentDirections.actionTvInfoFragmentToSearchResultFragment(
                searchQuery
            )
        findNavController().navigate(action)
    }
}