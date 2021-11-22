package com.moviesearcher.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.databinding.FragmentWatchlistBinding
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
    private lateinit var movieCardView: CardView
    private lateinit var tvCardView: CardView

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

        movieCardView = binding.cardviewMoviesWatchlist
        tvCardView = binding.cardviewTvsWatchlist
        movieCardView.visibility = View.INVISIBLE
        tvCardView.visibility = View.INVISIBLE
            movieRecyclerView = binding.movieRecyclerView
        tvRecyclerView = binding.tvRecyclerView
        movieRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        navController = findNavController()
        progressBar = binding.progressBarWatchlistFragment
        progressBar.visibility = View.VISIBLE

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

                        progressBar.visibility = View.GONE
                        movieCardView.visibility = View.VISIBLE
                        tvCardView.visibility = View.VISIBLE
                    })
            })
    }

    private fun createMovieAdapter(
        movieItems: MovieWatchlistResponse
    ): MovieWatchlistAdapter {
        val movieWatchlistAdapter = MovieWatchlistAdapter(
            movieItems,
            navController,
            accountId,
            sessionId,
            movieWatchlistViewModel.getMovieWatchlistIds(),
        )
        movieWatchlistAdapter.differ.submitList(movieItems.results)

        return movieWatchlistAdapter
    }

    private fun createTvAdapter(
        tvItems: TvWatchlistResponse
    ): TvWatchlistAdapter {
        val tvWatchlistAdapter = TvWatchlistAdapter(
            tvItems,
            navController,
            accountId,
            sessionId,
            tvWatchlistViewModel.getTvWatchlistIds(),
        )
        tvWatchlistAdapter.differ.submitList(tvItems.results)

        return tvWatchlistAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}