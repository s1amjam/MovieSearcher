package com.moviesearcher.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.databinding.FragmentWatchlistBinding
import com.moviesearcher.movie.adapter.TrendingAdapter
import com.moviesearcher.watchlist.movie.adapter.MovieWatchlistAdapter
import com.moviesearcher.watchlist.movie.model.MovieWatchlistResponse
import com.moviesearcher.watchlist.movie.viewmodel.MovieWatchlistViewModel
import com.moviesearcher.watchlist.tv.adapter.TvWatchlistAdapter
import com.moviesearcher.watchlist.tv.model.TvWatchlistResponse
import com.moviesearcher.watchlist.tv.viewmodel.TvWatchlistViewModel

private const val TAG = "WatchlistFragment"

class WatchlistFragment : BaseFragment() {
    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = _binding!!

    private val movieWatchlistViewModel: MovieWatchlistViewModel by viewModels()
    private val tvWatchlistViewModel: TvWatchlistViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var tvRecyclerView: RecyclerView
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

        movieRecyclerView = binding.movieRecyclerView
        tvRecyclerView = binding.tvRecyclerView
        movieRecyclerView.layoutManager = LinearLayoutManager(context)
        navController = findNavController()
        progressBar = binding.progressBarWatchlistFragment

        setupWatchlistUi()
    }

    private fun setupWatchlistUi() {
        movieWatchlistViewModel.getMovieWatchlist(accountId, sessionId).observe(
            viewLifecycleOwner,
            { movieItems ->
                tvWatchlistViewModel.getTvWatchlist(accountId, sessionId).observe(
                    viewLifecycleOwner,
                    { tvItems ->
                        val movieAdapter = createMovieAdapter(movieItems)
                        val tvAdapter = createTvAdapter(tvItems)
                        setupUi(movieAdapter, movieRecyclerView)
                        setupUi(tvAdapter, tvRecyclerView)
                    })
            })
    }

    private fun createMovieAdapter(
        movieItems: MovieWatchlistResponse
    ): MovieWatchlistAdapter {
        return MovieWatchlistAdapter(
            movieItems,
            navController,
            accountId,
            sessionId,
            movieWatchlistViewModel.getMovieWatchlistIds(),
        )
    }

    private fun createTvAdapter(
        tvItems: TvWatchlistResponse
    ): TvWatchlistAdapter {
        return TvWatchlistAdapter(
            tvItems,
            navController,
            accountId,
            sessionId,
            movieWatchlistViewModel.getMovieWatchlistIds(),
        )
    }

    override fun setupUi(
        _adapter: RecyclerView.Adapter<*>,
        recyclerView: RecyclerView
    ) {
        recyclerView.apply {
            addItemDecoration(
                TrendingAdapter.GridSpacingItemDecoration(
                    100,
                    10,
                    true
                )
            )
        }

        progressBar.visibility = View.VISIBLE
        super.setupUi(_adapter, recyclerView)
        progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}