package com.moviesearcher.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.common.utils.Status
import com.moviesearcher.common.viewmodel.ViewModelFactory
import com.moviesearcher.databinding.FragmentWatchlistBinding
import com.moviesearcher.watchlist.common.viewmodel.WatchlistViewModel
import com.moviesearcher.watchlist.movie.adapter.MovieWatchlistAdapter
import com.moviesearcher.watchlist.movie.model.MovieWatchlistResponse
import com.moviesearcher.watchlist.tv.model.MovieWatchlistResult

private const val TAG = "WatchlistFragment"

class WatchlistFragment : BaseFragment() {
    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: WatchlistViewModel

    private lateinit var navController: NavController
    private lateinit var watchlistRv: RecyclerView
    private lateinit var movieWatchlistButton: Button
    private lateinit var tvWatchlistButton: Button
    private lateinit var progressBar: ProgressBar

    private var movieWatchlistIds: MutableList<Long> = mutableListOf()
    private var tvWatchlistIds: MutableList<Long> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        watchlistRv = binding.watchlistRv
        movieWatchlistButton = binding.moviesWatchlistButton
        tvWatchlistButton = binding.tvWatchlistButton
        progressBar = binding.watchlistPb

        setupViewModel()
        setupMovieUi()

        movieWatchlistButton.setOnClickListener {
            setupMovieUi()
        }

        tvWatchlistButton.setOnClickListener {
            setupTvUi()
        }
    }

    private fun setupMovieUi() {
        viewModel.getMovieWatchlist().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { movieItems ->
                        movieItems.results?.forEach { it -> movieWatchlistIds.add(it.id!!.toLong()) }
                        createWatchlistAdapter(movieItems)

                        progressBar.visibility = View.GONE
                        watchlistRv.visibility = View.VISIBLE
                    }
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    watchlistRv.visibility = View.GONE
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

    private fun setupTvUi() {
        viewModel.getTvWatchlist().observe(viewLifecycleOwner) { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { tvItems ->
                        tvItems.results?.forEach { it -> tvWatchlistIds.add(it.id!!.toLong()) }
                        val movieWatchlistResponse = MovieWatchlistResponse().apply {
                            val list = mutableListOf<MovieWatchlistResult>()
                            tvItems.results?.forEach {
                                list.add(
                                    MovieWatchlistResult(
                                        id = it.id,
                                        title = it.name,
                                        overview = it.overview,
                                        voteAverage = it.voteAverage,
                                        releaseDate = it.firstAirDate,
                                        posterPath = it.posterPath
                                    )
                                )
                            }

                            results = list
                        }
                        createWatchlistAdapter(movieWatchlistResponse, true)

                        progressBar.visibility = View.GONE
                        watchlistRv.visibility = View.VISIBLE
                    }
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    watchlistRv.visibility = View.GONE
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

    private fun createWatchlistAdapter(
        movieItems: MovieWatchlistResponse,
        isTv: Boolean = false
    ): MovieWatchlistAdapter {
        val movieWatchlistAdapter = MovieWatchlistAdapter(
            movieItems,
            navController,
            viewModel,
            requireContext(),
            isTv
        )
        watchlistRv.apply {
            watchlistRv.adapter = movieWatchlistAdapter
            layoutManager = LinearLayoutManager(context)
        }

        movieWatchlistAdapter.differ.submitList(movieItems.results)

        return movieWatchlistAdapter
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                sessionId = sessionId, accountId = accountId
            )
        ).get(WatchlistViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding?.watchlistRv?.adapter = null
        _binding = null
    }
}