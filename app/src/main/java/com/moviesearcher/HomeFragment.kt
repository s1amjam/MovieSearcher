package com.moviesearcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieSearcherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainLayout = binding.movieConstraintLayout
        mainLayout.visibility = View.INVISIBLE
        navController = findNavController()
        progressBar = binding.progressBarMovieSearcherFragment
        progressBar.visibility = View.VISIBLE
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
            viewModel.getMovieWatchlist(accountId, sessionId)
                .observe(viewLifecycleOwner, {
                    setupTrendingMoviesUi()
                })

            viewModel.getTvWatchlist(accountId, sessionId)
                .observe(viewLifecycleOwner, {
                    setupTrendingTvsUi()
                })
        }
    }

    private fun setupTrendingMoviesUi() {
        viewModel.getTrendingMovies().observe(
            viewLifecycleOwner,
            { movieItems ->
                val adapter = createAdapter(movieItems)
                setupUi(adapter, movieRecyclerView)
            })
    }

    private fun createAdapter(movieItems: TrendingResponse): TrendingAdapter {
        val trendingAdapter = TrendingAdapter(
            movieItems,
            navController,
            accountId,
            sessionId,
            viewModel.getMovieWatchlistIds()
        )
        trendingAdapter.differ.submitList(movieItems.results)

        return trendingAdapter
    }

    private fun setupTrendingTvsUi() {
        viewModel.getTrendingTvs().observe(
            viewLifecycleOwner,
            { tvItems ->
                val adapter = createAdapter(tvItems)
                setupUi(adapter, tvRecyclerView)
                progressBar.visibility = View.GONE
                mainLayout.visibility = View.VISIBLE
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding?.movieRecyclerView?.adapter = null
        _binding?.tvRecyclerView?.adapter = null
        _binding = null
    }
}