package com.moviesearcher.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
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
import com.moviesearcher.watchlist.tv.adapter.TvWatchlistAdapter
import com.moviesearcher.watchlist.tv.model.TvWatchlistResponse

private const val TAG = "WatchlistFragment"

class WatchlistFragment : BaseFragment() {
    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: WatchlistViewModel

    private lateinit var navController: NavController
    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var tvRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var movieCardView: CardView
    private lateinit var tvCardView: CardView

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

        movieCardView = binding.cardviewMoviesWatchlist
        tvCardView = binding.cardviewTvsWatchlist
        movieRecyclerView = binding.movieRecyclerView
        tvRecyclerView = binding.tvRecyclerView
        movieRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        navController = findNavController()
        progressBar = binding.progressBarWatchlistFragment

        setupViewModel()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.getMovieWatchlist().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { movieItems ->
                        movieItems.results?.forEach { it -> movieWatchlistIds.add(it.id!!.toLong()) }
                        val movieAdapter = createMovieAdapter(movieItems)
                        setupUi(movieAdapter, movieRecyclerView)

                        progressBar.visibility = View.GONE
                        movieCardView.visibility = View.VISIBLE
                    }
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    movieCardView.visibility = View.GONE
                }
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        ERROR_MESSAGE.format(it.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        viewModel.getTvWatchlist().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { tvItems ->
                        tvItems.results?.forEach { it -> tvWatchlistIds.add(it.id!!.toLong()) }
                        val tvAdapter = createTvAdapter(tvItems)
                        setupUi(tvAdapter, tvRecyclerView)

                        progressBar.visibility = View.GONE
                        tvCardView.visibility = View.VISIBLE
                    }
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    tvCardView.visibility = View.GONE
                }
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        ERROR_MESSAGE.format(it.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun createMovieAdapter(
        movieItems: MovieWatchlistResponse
    ): MovieWatchlistAdapter {
        val movieWatchlistAdapter = MovieWatchlistAdapter(
            movieItems,
            navController,
            accountId,
            sessionId,
            movieWatchlistIds,
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
            tvWatchlistIds,
        )
        tvWatchlistAdapter.differ.submitList(tvItems.results)

        return tvWatchlistAdapter
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
        _binding = null
    }
}