package com.moviesearcher.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.common.viewmodel.BaseViewModel
import com.moviesearcher.databinding.FragmentSearchResultBinding
import com.moviesearcher.search.adapter.SearchAdapter

private const val TAG = "SearchResultFragment"

class SearchResultFragment : BaseFragment() {
    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchResultRecyclerView: RecyclerView
    private val viewModel: BaseViewModel by viewModels()

    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchResultRecyclerView = binding.searchResultRecyclerView
        progressBar = binding.progressBarSearch
        searchResultRecyclerView.visibility = View.INVISIBLE
        val searchView = binding.searchView

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(queryText: String): Boolean {
                    if (queryText != "") {
                        progressBar.visibility = View.VISIBLE
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
        viewModel.queryForSearch(searchQuery).observe(
            viewLifecycleOwner,
            { searchItems ->
                val searchAdapter = SearchAdapter(searchItems, findNavController())

                searchResultRecyclerView.apply {
                    adapter = searchAdapter
                    layoutManager = LinearLayoutManager(context)
                }
                searchAdapter.differ.submitList(searchItems.results)

                progressBar.visibility = View.GONE
                searchResultRecyclerView.visibility = View.VISIBLE
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}