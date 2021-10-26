package com.moviesearcher

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
import com.moviesearcher.databinding.FragmentMovieSearcherBinding
import com.moviesearcher.movie.adapter.TrendingAdapter
import com.moviesearcher.movie.model.TrendingResponse
import com.moviesearcher.movie.viewmodel.TrendingViewModel
import com.moviesearcher.watchlist.movie.viewmodel.MovieWatchlistViewModel
import com.moviesearcher.watchlist.tv.viewmodel.TvWatchlistViewModel

private const val TAG = "MovieSearcherFragment"

class HomeFragment : BaseFragment() {
    private var _binding: FragmentMovieSearcherBinding? = null
    private val binding get() = _binding!!

    private val trendingViewModel: TrendingViewModel by viewModels()
    private val movieWatchlistViewModel: MovieWatchlistViewModel by viewModels()
    private val tvWatchlistViewModel: TvWatchlistViewModel by viewModels()

    private lateinit var navController: NavController
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
        movieRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        tvRecyclerView = binding.tvRecyclerView
        tvRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        getWatchlistIfLogged()
        setupTrendingMoviesUi()
        setupTrendingTvsUi()
    }

    private fun getWatchlistIfLogged() {
        if (sessionId.isNotBlank() || sessionId != null) {
            movieWatchlistViewModel.getMovieWatchlist(accountId, sessionId)
                .observe(viewLifecycleOwner, {
                    setupTrendingMoviesUi()
                })

            tvWatchlistViewModel.getTvWatchlist(accountId, sessionId)
                .observe(viewLifecycleOwner, {
                    setupTrendingTvsUi()
                })
        }
    }

    private fun setupTrendingMoviesUi() {
        trendingViewModel.trendingMovies.observe(
            viewLifecycleOwner,
            { movieItems ->
                val adapter = createAdapter(movieItems)
                setupUi(adapter, movieRecyclerView)
            })
    }

    private fun createAdapter(movieItems: TrendingResponse): TrendingAdapter {
        return TrendingAdapter(
            movieItems,
            navController,
            accountId,
            sessionId,
            movieWatchlistViewModel.getMovieWatchlistIds()
        )
    }

    private fun setupTrendingTvsUi() {
        trendingViewModel.trendingTvs.observe(
            viewLifecycleOwner,
            { tvItems ->
                val adapter = createAdapter(tvItems)
                setupUi(adapter, tvRecyclerView)
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
        progressBar.visibility = View.VISIBLE
        super.setupUi(_adapter, recyclerView)
        progressBar.visibility = View.GONE
    }
}