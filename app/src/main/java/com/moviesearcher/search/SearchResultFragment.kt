package com.moviesearcher.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.databinding.FragmentSearchResultBinding
import com.moviesearcher.search.adapter.SearchAdapter
import com.moviesearcher.search.viewmodel.SearchViewModel

private const val TAG = "SearchResultFragment"

class SearchResultFragment : BaseFragment() {
    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchResultRecyclerView: RecyclerView
    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        val view = binding.root

        searchResultRecyclerView = binding.searchResultRecyclerView
        searchResultRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = binding.searchView

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(queryText: String): Boolean {
                    if (queryText != "") {
                        updateWithSearchResult(queryText)
                    }
                    return true
                }

                override fun onQueryTextSubmit(searchQuery: String): Boolean {
                    return false
                }
            })
        }

        searchResultRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                hideKeyboard(view)
            }
        })
    }

    private fun updateWithSearchResult(searchQuery: String) {
        searchViewModel.queryForSearch(searchQuery).observe(
            viewLifecycleOwner,
            { searchItems ->
                searchResultRecyclerView.adapter = SearchAdapter(searchItems, findNavController())
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}