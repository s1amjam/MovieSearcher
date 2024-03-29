package com.moviesearcher.watchlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.utils.Constants.ERROR_MESSAGE
import com.moviesearcher.common.utils.Status
import com.moviesearcher.databinding.FragmentWatchlistBinding
import com.moviesearcher.watchlist.common.model.WatchlistRequest
import com.moviesearcher.watchlist.common.viewmodel.WatchlistViewModel
import com.moviesearcher.watchlist.movie.adapter.MovieWatchlistAdapter
import com.moviesearcher.watchlist.movie.model.MovieWatchlistResponse
import com.moviesearcher.watchlist.tv.model.MovieWatchlistResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchlistFragment : Fragment() {
    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<WatchlistViewModel>()

    private lateinit var navController: NavController
    private lateinit var watchlistRv: RecyclerView
    private lateinit var movieWatchlistButton: Button
    private lateinit var tvWatchlistButton: Button
    private lateinit var progressBar: ProgressBar

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
            isTv,
            object : MovieWatchlistAdapter.ItemClickListener {
                override fun onItemClick(
                    button: ImageButton,
                    media: MutableMap<String, Long>,
                    context: Context,
                    lifecycleOwner: LifecycleOwner
                ) {
                    viewModel.postWatchlist(
                        WatchlistRequest(
                            button.tag.toString().toBoolean(),
                            media.values.first(),
                            media.keys.first()
                        )
                    )

                    viewModel.processWatchlistButtons(button)
                }
            }
        )

        watchlistRv.apply {
            watchlistRv.adapter = movieWatchlistAdapter
            layoutManager = LinearLayoutManager(context)
        }

        movieWatchlistAdapter.differ.submitList(movieItems.results)

        return movieWatchlistAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding?.watchlistRv?.adapter = null
        _binding = null
    }
}