package com.moviesearcher.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.R
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.watchlist.movie.adapter.MovieWatchlistAdapter
import com.moviesearcher.watchlist.movie.viewmodel.MovieWatchlistViewModel
import com.moviesearcher.watchlist.tv.adapter.TvWatchlistAdapter
import com.moviesearcher.watchlist.tv.viewmodel.TvWatchlistViewModel

private const val TAG = "WatchlistFragment"

class WatchlistFragment : BaseFragment() {
    private lateinit var movieWatchlistRecyclerView: RecyclerView
    private val movieWatchlistViewModel: MovieWatchlistViewModel by viewModels()
    private lateinit var tvWatchlistViewModel: TvWatchlistViewModel
    private lateinit var movieWatchlistButton: Button
    private lateinit var tvWatchlistButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_watchlist, container, false
        )

        movieWatchlistRecyclerView = view.findViewById(R.id.fragment_watchlist_recycler_view)
        movieWatchlistRecyclerView.layoutManager = LinearLayoutManager(context)
        tvWatchlistViewModel = ViewModelProvider(this).get(TvWatchlistViewModel::class.java)
        movieWatchlistButton = view.findViewById(R.id.button_watchlist_movies)
        tvWatchlistButton = view.findViewById(R.id.button_watchlist_tvs)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }
}