package com.moviesearcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.common.utils.Status
import com.moviesearcher.common.viewmodel.BaseViewModel
import com.moviesearcher.databinding.FragmentMovieSearcherBinding
import com.moviesearcher.movie.adapter.TrendingAdapter
import com.moviesearcher.movie.model.TrendingResponse

private const val TAG = "HomeFragment"

class HomeFragment : BaseFragment() {
    private var _binding: FragmentMovieSearcherBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BaseViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var trendingMovieRecyclerView: RecyclerView
    private lateinit var trendingTvRecyclerView: RecyclerView
    private lateinit var upcomingMovieRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var mainLayout: ConstraintLayout

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

        trendingMovieRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        trendingTvRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        upcomingMovieRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        getWatchlistIfLogged()
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
                    trendingMovieRecyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    trendingMovieRecyclerView.visibility = View.GONE
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
                    trendingTvRecyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    trendingTvRecyclerView.visibility = View.GONE
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
                    upcomingMovieRecyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    upcomingMovieRecyclerView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun getWatchlistIfLogged() {
        if (sessionId.isNotEmpty()) {
            viewModel.getMovieWatchlist(accountId, sessionId)
                .observe(viewLifecycleOwner, {
                    viewModel.getTvWatchlist(accountId, sessionId)
                        .observe(viewLifecycleOwner, {
                            setupObserver()
                            watchlistIds = viewModel.getMovieWatchlistIds()
                        })
                })
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding?.movieRecyclerView?.adapter = null
        _binding?.tvRecyclerView?.adapter = null
        _binding = null
    }
}