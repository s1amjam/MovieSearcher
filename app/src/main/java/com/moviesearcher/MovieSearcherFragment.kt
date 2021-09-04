package com.moviesearcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.databinding.FragmentMovieSearcherBinding
import com.moviesearcher.movie.adapter.TrendingAdapter
import com.moviesearcher.movie.viewmodel.TrendingViewModel
import com.moviesearcher.watchlist.movie.viewmodel.MovieWatchlistViewModel
import com.moviesearcher.watchlist.tv.viewmodel.TvWatchlistViewModel

private const val TAG = "MovieSearcherFragment"

class MovieSearcherFragment : BaseFragment() {
    private var _binding: FragmentMovieSearcherBinding? = null
    private val binding get() = _binding!!

    private val trendingViewModel: TrendingViewModel by viewModels()
    private val movieWatchlistViewModel: MovieWatchlistViewModel by viewModels()
    private val tvWatchlistViewModel: TvWatchlistViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var trendingAdapter: TrendingAdapter
    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var tvRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

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
        progressBar = binding.progressBarMovieSearcherFragment
        movieRecyclerView = binding.movieRecyclerView
        tvRecyclerView = binding.tvRecyclerView

        getWatchlistIfLogged()
        setupTrendingMoviesUi()
        setupTrendingTvsUi()
    }

    private fun getWatchlistIfLogged() {
        if (sessionId.isNotBlank()) {
            movieWatchlistViewModel.getMovieWatchlist(accountId, sessionId)
                .observe(viewLifecycleOwner, {})

            tvWatchlistViewModel.getTvWatchlist(accountId, sessionId)
                .observe(viewLifecycleOwner, {})
        }
    }

    private fun setupTrendingMoviesUi() {
        trendingViewModel.trendingMovies.observe(
            viewLifecycleOwner,
            { movieItems ->
                trendingAdapter = TrendingAdapter(
                    movieItems,
                    navController,
                    accountId,
                    sessionId,
                    movieWatchlistViewModel.getMovieWatchlistIds()
                )
                setupUi(trendingAdapter, movieRecyclerView)
            })
    }

    private fun setupTrendingTvsUi() {
        trendingViewModel.trendingTvs.observe(
            viewLifecycleOwner,
            { tvItems ->
                trendingAdapter =
                    TrendingAdapter(
                        tvItems,
                        navController,
                        accountId,
                        sessionId,
                        tvWatchlistViewModel.getTvWatchlistIds()
                    )
                setupUi(trendingAdapter, tvRecyclerView)
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding?.movieRecyclerView?.adapter = null
        _binding?.tvRecyclerView?.adapter = null
        _binding = null
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
        view?.visibility = View.VISIBLE
    }
}