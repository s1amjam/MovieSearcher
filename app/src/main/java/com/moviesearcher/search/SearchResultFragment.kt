package com.moviesearcher.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
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

    private lateinit var nothingWasFoundTv: TextView
    private lateinit var searchView: SearchView

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
        nothingWasFoundTv = binding.nothingFoundTv
        searchView = binding.searchView
        searchResultRecyclerView.visibility = View.INVISIBLE
        nothingWasFoundTv.visibility = View.INVISIBLE

        searchView.requestFocus()

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

                if (searchItems.totalResults!! <= 0) {
                    nothingWasFoundTv.visibility = View.VISIBLE
                } else {
                    nothingWasFoundTv.visibility = View.INVISIBLE
                }

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

    override fun onResume() {
        super.onResume()

        val inputMethodService =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodService.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}