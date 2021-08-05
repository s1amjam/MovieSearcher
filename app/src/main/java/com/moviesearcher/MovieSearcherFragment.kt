package com.moviesearcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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

    private lateinit var movieRecyclerView: RecyclerView
    private val movieViewModel: MovieViewModel by viewModels()
    private val tvViewModel: TvViewModel by viewModels()

    private lateinit var trendingMovieButton: Button
    private lateinit var trendingTvButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieSearcherBinding.inflate(inflater, container, false)
        val view = binding.root

        movieRecyclerView = binding.movieRecyclerView
        trendingMovieButton = binding.trendingMovieButton
        trendingTvButton = binding.trendingTvButton
        movieRecyclerView.layoutManager = GridLayoutManager(context, 3)
        progressBar = binding.progressBarMovieSearcherFragment

        //TODO: if we are going back from tv info, movies showing instead of tv
        trendingTvButton.setOnClickListener {
            tvViewModel.tvs.observe(
                viewLifecycleOwner,
                { movieItems ->
                    movieRecyclerView.adapter = MovieAdapter(movieItems, findNavController())
                })
        }

        trendingMovieButton.setOnClickListener {
            movieViewModel.trendingMovies.observe(
                viewLifecycleOwner,
                { movieItems ->
                    movieRecyclerView.adapter = MovieAdapter(movieItems, findNavController())
                })
        }

        progressBar.visibility = VISIBLE

        movieViewModel.trendingMovies.observe(
            viewLifecycleOwner,
            { movieItems ->
                movieRecyclerView.adapter = MovieAdapter(movieItems, findNavController())
                progressBar.visibility = GONE
            })

        movieRecyclerView.addItemDecoration(
            MovieAdapter.GridSpacingItemDecoration(
                3,
                5,
                true
            )
        )
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}