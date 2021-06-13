package com.moviesearcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.adapters.SearchAdapter
import com.moviesearcher.viewmodel.SearchViewModel

private const val TAG = "SearchResultFragment"

class SearchResultFragment : Fragment() {
    private lateinit var searchResultRecyclerView: RecyclerView
    private lateinit var searchViewModel: SearchViewModel
    private val args by navArgs<SearchResultFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_result, container, false)
        searchResultRecyclerView = view.findViewById(R.id.search_result_recycler_view)
        searchResultRecyclerView.layoutManager = LinearLayoutManager(context)
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchQuery = args.searchQuery
        searchViewModel.queryForSearch(searchQuery)

        searchViewModel.searchItemLiveData.observe(
            viewLifecycleOwner,
            { searchItems ->
                searchResultRecyclerView.adapter = SearchAdapter(searchItems)
            })
    }
}