package com.moviesearcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.databinding.FragmentMovieSearcherBinding
import com.moviesearcher.movie.adapter.MovieAdapter
import com.moviesearcher.movie.viewmodel.MovieViewModel
import com.moviesearcher.tv.viewmodel.TvViewModel

private const val TAG = "MovieSearcherFragment"

class MovieSearcherFragment : BaseFragment() {
    private var _binding: FragmentMovieSearcherBinding? = null
    private val binding get() = _binding!!

    private val movieViewModel: MovieViewModel by viewModels()
    private val tvViewModel: TvViewModel by viewModels()
    private lateinit var navController: NavController
    private var spanCount: Int = 3
    lateinit var movieAdapter: MovieAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var trendingMovieButton: Button
    private lateinit var trendingTvButton: Button
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
        trendingMovieButton = binding.trendingMovieButton
        trendingTvButton = binding.trendingTvButton
        progressBar = binding.progressBarMovieSearcherFragment
        recyclerView = binding.movieRecyclerView

        trendingTvButton.setOnClickListener {
            tvViewModel.tvs.observe(
                viewLifecycleOwner,
                { tvItems ->
                    movieAdapter = MovieAdapter(tvItems, navController)
                    setupUi(movieAdapter, recyclerView, spanCount)
                })
        }

        trendingMovieButton.setOnClickListener {
            movieViewModel.trendingMovies.observe(
                viewLifecycleOwner,
                { movieItems ->
                    movieAdapter = MovieAdapter(movieItems, navController)
                    setupUi(movieAdapter, recyclerView, spanCount)
                })
        }

        movieViewModel.trendingMovies.observe(
            viewLifecycleOwner,
            { movieItems ->
                movieAdapter = MovieAdapter(movieItems, navController)
                setupUi(movieAdapter, recyclerView, spanCount)
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding?.movieRecyclerView?.adapter = null
        _binding = null
    }

    override fun setupUi(
        _adapter: RecyclerView.Adapter<*>,
        recyclerView: RecyclerView,
        spanCount: Int
    ) {
        progressBar.visibility = View.VISIBLE
        super.setupUi(_adapter, recyclerView, spanCount)
        progressBar.visibility = View.GONE
    }

    override fun setupRecyclerView(
        _adapter: RecyclerView.Adapter<*>,
        recyclerView: RecyclerView,
        spanCount: Int
    ) {
        super.setupRecyclerView(_adapter, recyclerView, spanCount)

        binding.movieRecyclerView.apply {
            addItemDecoration(
                MovieAdapter.GridSpacingItemDecoration(
                    3,
                    3,
                    true
                )
            )
        }
    }
}