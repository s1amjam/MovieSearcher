package com.moviesearcher.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.databinding.FragmentWatchlistBinding
import com.moviesearcher.watchlist.movie.adapter.MovieWatchlistAdapter
import com.moviesearcher.watchlist.movie.viewmodel.MovieWatchlistViewModel
import com.moviesearcher.watchlist.tv.adapter.TvWatchlistAdapter
import com.moviesearcher.watchlist.tv.viewmodel.TvWatchlistViewModel

private const val TAG = "WatchlistFragment"

class WatchlistFragment : BaseFragment() {
    private var _binding: FragmentWatchlistBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieWatchlistRecyclerView: RecyclerView
    private val movieWatchlistViewModel: MovieWatchlistViewModel by viewModels()
    private val tvWatchlistViewModel: TvWatchlistViewModel by viewModels()

    private lateinit var movieWatchlistButton: Button
    private lateinit var tvWatchlistButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchlistBinding.inflate(inflater, container, false)
        val view = binding.root

        movieWatchlistRecyclerView = binding.fragmentWatchlistRecyclerView
        movieWatchlistRecyclerView.layoutManager = LinearLayoutManager(context)
        movieWatchlistButton = binding.buttonWatchlistMovies
        tvWatchlistButton = binding.buttonWatchlistTvs

        movieWatchlistViewModel.getMovieWatchlist(accountId, sessionId).observe(
            viewLifecycleOwner,
            { movieWatchlistItem ->
                movieWatchlistRecyclerView.adapter =
                    MovieWatchlistAdapter(movieWatchlistItem, findNavController())
            })

        movieWatchlistButton.setOnClickListener {
            movieWatchlistViewModel.getMovieWatchlist(accountId, sessionId).observe(
                viewLifecycleOwner,
                { movieWatchlistItem ->
                    movieWatchlistRecyclerView.adapter =
                        MovieWatchlistAdapter(movieWatchlistItem, findNavController())
                })
        }

        tvWatchlistButton.setOnClickListener {
            tvWatchlistViewModel.getTvWatchlist(accountId, sessionId).observe(
                viewLifecycleOwner,
                { tvWatchlistItem ->
                    movieWatchlistRecyclerView.adapter =
                        TvWatchlistAdapter(tvWatchlistItem, findNavController())
                })
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}