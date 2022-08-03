package com.moviesearcher.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.common.utils.Status
import com.moviesearcher.common.viewmodel.ViewModelFactory
import com.moviesearcher.databinding.FragmentSearchResultBinding
import com.moviesearcher.search.adapter.SearchAdapter

private const val TAG = "SearchResultFragment"

class SearchResultFragment : BaseFragment() {
    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchResultRecyclerView: RecyclerView
    private lateinit var viewModel: SearchViewModel

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

        nothingWasFoundTv.visibility = View.INVISIBLE

        setupViewModel()

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
        viewModel.queryForSearch(searchQuery)
        viewModel.getSearchResult().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { searchItems ->
                        if (searchItems.totalResults!! <= 0) {
                            nothingWasFoundTv.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                        } else {
                            val searchAdapter = SearchAdapter(searchItems, findNavController())

                            searchResultRecyclerView.apply {
                                adapter = searchAdapter
                                layoutManager = LinearLayoutManager(context)
                            }
                            searchAdapter.differ.submitList(searchItems.results)

                            progressBar.visibility = View.GONE
                            nothingWasFoundTv.visibility = View.INVISIBLE
                            searchResultRecyclerView.visibility = View.VISIBLE
                        }
                    }
                }
                Status.LOADING -> {
                    searchResultRecyclerView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    nothingWasFoundTv.visibility = View.INVISIBLE
                }
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        ERROR_MESSAGE.format(it.message),
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val inputMethodService =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodService.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory()
        ).get(SearchViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}