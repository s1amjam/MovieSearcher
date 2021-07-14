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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.adapters.SearchAdapter
import com.moviesearcher.viewmodel.SearchViewModel

private const val TAG = "SearchResultFragment"

class SearchResultFragment : Fragment() {
    private lateinit var searchResultRecyclerView: RecyclerView
    private val searchViewModel: SearchViewModel by viewModels()
    private val args by navArgs<SearchResultFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_movie_searcher_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(searchQuery: String): Boolean {
                    return false
                }

                override fun onQueryTextChange(queryText: String): Boolean {
                    if (queryText != "") {
                        searchViewModel.queryForSearch(queryText)
                    }
                    return true
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_result, container, false)
        searchResultRecyclerView = view.findViewById(R.id.search_result_recycler_view)
        searchResultRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchQuery = args.searchQuery
        searchViewModel.queryForSearch(searchQuery).observe(
            viewLifecycleOwner,
            { searchItems ->
                searchResultRecyclerView.adapter = SearchAdapter(searchItems, findNavController())
            })
    }
}