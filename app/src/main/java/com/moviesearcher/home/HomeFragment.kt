package com.moviesearcher.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.R
import com.moviesearcher.common.utils.Constants.ERROR_MESSAGE
import com.moviesearcher.common.utils.Status
import com.moviesearcher.databinding.FragmentMovieSearcherBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentMovieSearcherBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var trendingMovieRecyclerView: RecyclerView
    private lateinit var trendingTvRecyclerView: RecyclerView
    private lateinit var upcomingMovieRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var trendingMoviesCardView: CardView
    private lateinit var trendingTvsCardView: CardView
    private lateinit var upcomingMoviesCardView: CardView
    private lateinit var featuredLl: LinearLayout
    private lateinit var upcomingLl: LinearLayout
    private lateinit var tvsTitle: TextView

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
        trendingMovieRecyclerView = binding.moviesCardview.recyclerView
        trendingTvRecyclerView = binding.tvsCardview.recyclerView
        upcomingMovieRecyclerView = binding.upcomingCardview.recyclerView
        trendingMoviesCardView = binding.moviesCardview.cardView
        trendingTvsCardView = binding.tvsCardview.cardView
        upcomingMoviesCardView = binding.upcomingCardview.cardView
        featuredLl = binding.featuredCl
        upcomingLl = binding.upcomingCl
        tvsTitle = binding.tvsCardview.titleTextview

        tvsTitle.text = getString(R.string.tvs)

        trendingMovieRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        trendingTvRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        upcomingMovieRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.getTrendingMovies().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { trendingMovies ->
                        val adapter = createAdapter()
                        trendingMovieRecyclerView.adapter = adapter
                        adapter.submitList(trendingMovies.results)
                    }
                    progressBar.visibility = View.GONE
                    featuredLl.visibility = View.VISIBLE
                    trendingMoviesCardView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    featuredLl.visibility = View.GONE
                    trendingMoviesCardView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
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

        viewModel.getTrendingTvs().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { trendingTvs ->
                        val adapter = createAdapter()
                        trendingTvRecyclerView.adapter = adapter
                        adapter.submitList(trendingTvs.results)
                    }
                    progressBar.visibility = View.GONE
                    featuredLl.visibility = View.VISIBLE
                    trendingTvsCardView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    featuredLl.visibility = View.GONE
                    trendingTvsCardView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
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

        viewModel.getUpcomingMovies().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { upcomingMovies ->
                        val adapter = createAdapter()
                        upcomingMovieRecyclerView.adapter = adapter
                        adapter.submitList(upcomingMovies.results)
                    }
                    progressBar.visibility = View.GONE
                    upcomingLl.visibility = View.VISIBLE
                    upcomingMoviesCardView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    upcomingLl.visibility = View.GONE
                    upcomingMoviesCardView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
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

    private fun createAdapter(): HomeAdapter {
        return HomeAdapter(navController)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding?.moviesCardview?.recyclerView?.adapter = null
        _binding?.tvsCardview?.recyclerView?.adapter = null
        _binding?.upcomingCardview?.recyclerView?.adapter = null
        _binding = null
    }
}