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
    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var tvRecyclerView: RecyclerView
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
        movieRecyclerView = binding.movieRecyclerView
        tvRecyclerView = binding.tvRecyclerView
        movieRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        tvRecyclerView.layoutManager =
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
                        movieRecyclerView.adapter = adapter
                    }
                    progressBar.visibility = View.GONE
                    movieRecyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    movieRecyclerView.visibility = View.GONE
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
                        tvRecyclerView.adapter = adapter
                    }
                    progressBar.visibility = View.GONE
                    tvRecyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    tvRecyclerView.visibility = View.GONE
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