package com.moviesearcher.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.common.utils.Status
import com.moviesearcher.common.viewmodel.ViewModelFactory
import com.moviesearcher.databinding.FragmentMovieSearcherBinding
import com.moviesearcher.movie.adapter.TrendingAdapter
import com.moviesearcher.movie.model.TrendingResponse
import com.moviesearcher.watchlist.common.viewmodel.WatchlistViewModel

private const val TAG = "HomeFragment"

class HomeFragment : BaseFragment() {
    private var _binding: FragmentMovieSearcherBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var watchlistViewModel: WatchlistViewModel

    private lateinit var navController: NavController
    private lateinit var trendingMovieRecyclerView: RecyclerView
    private lateinit var trendingTvRecyclerView: RecyclerView
    private lateinit var upcomingMovieRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var mainLayout: ConstraintLayout
    private lateinit var trendingMoviesCardView: CardView
    private lateinit var trendingTvsCardView: CardView
    private lateinit var upcomingMoviesCardView: CardView

    private var watchlistIds: MutableList<Long>? = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieSearcherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        mainLayout = binding.movieConstraintLayout
        progressBar = binding.progressBarMovieSearcherFragment
        trendingMovieRecyclerView = binding.movieRecyclerView
        trendingTvRecyclerView = binding.tvRecyclerView
        upcomingMovieRecyclerView = binding.upcomingMoviesRecyclerView
        trendingMoviesCardView = binding.cardviewMoviesFeaturedToday
        trendingTvsCardView = binding.cardviewTvsFeaturedToday
        upcomingMoviesCardView = binding.cardviewUpcomingMovies

        trendingMovieRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        trendingTvRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        upcomingMovieRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        setupWatchlist()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.getTrendingMovies().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { trendingMovies ->
                        val adapter = createAdapter(trendingMovies)
                        trendingMovieRecyclerView.adapter = adapter
                    }
                    progressBar.visibility = View.GONE
                    trendingMoviesCardView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    trendingMoviesCardView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        viewModel.getTrendingTvs().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { trendingTvs ->
                        val adapter = createAdapter(trendingTvs)
                        trendingTvRecyclerView.adapter = adapter
                    }
                    progressBar.visibility = View.GONE
                    trendingTvsCardView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    trendingTvsCardView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        viewModel.getUpcomingMovies().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { upcomingMovies ->
                        val adapter = createAdapter(upcomingMovies)
                        upcomingMovieRecyclerView.adapter = adapter
                    }
                    progressBar.visibility = View.GONE
                    upcomingMoviesCardView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    upcomingMoviesCardView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun createAdapter(movieItems: TrendingResponse): TrendingAdapter {
        val trendingAdapter = TrendingAdapter(
            movieItems,
            navController,
            accountId,
            sessionId,
            watchlistIds
        )
        trendingAdapter.differ.submitList(movieItems.results)

        return trendingAdapter
    }

    private fun setupWatchlist() {
        if (sessionId.isNotEmpty()) {
            watchlistViewModel = ViewModelProvider(
                this,
                ViewModelFactory(
                    sessionId, accountId
                )
            ).get(WatchlistViewModel::class.java)
        }

        watchlistViewModel.getWatchlistedItemsIds().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { movieItems ->
                        movieItems.forEach { it -> watchlistIds?.add(it) }
                        setupObserver()
                    }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding?.movieRecyclerView?.adapter = null
        _binding?.tvRecyclerView?.adapter = null
        _binding = null
    }
}