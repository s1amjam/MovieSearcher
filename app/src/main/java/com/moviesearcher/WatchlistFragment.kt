package com.moviesearcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.adapters.MovieWatchlistAdapter
import com.moviesearcher.adapters.TvWatchlistAdapter
import com.moviesearcher.utils.EncryptedSharedPrefs
import com.moviesearcher.viewmodel.MovieWatchlistViewModel
import com.moviesearcher.viewmodel.TvWatchlistViewModel

private const val TAG = "WatchlistFragment"

class WatchlistFragment : Fragment() {
    private lateinit var movieWatchlistRecyclerView: RecyclerView
    private lateinit var movieWatchlistViewModel: MovieWatchlistViewModel
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
        movieWatchlistViewModel = ViewModelProvider(this)
            .get(MovieWatchlistViewModel::class.java)
        tvWatchlistViewModel = ViewModelProvider(this).get(TvWatchlistViewModel::class.java)
        movieWatchlistButton = view.findViewById(R.id.button_watchlist_movies)
        tvWatchlistButton = view.findViewById(R.id.button_watchlist_tvs)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val encryptedSharedPrefs = EncryptedSharedPrefs.sharedPrefs(requireContext())
        val accountId: Int = encryptedSharedPrefs.getString("accountId", null)!!.toInt()
        val sessionId: String = encryptedSharedPrefs.getString("sessionId", null)!!

        movieWatchlistViewModel.getMovieWatchlist(accountId, sessionId)

        movieWatchlistViewModel.movieWatchlistItemLiveData.observe(
            viewLifecycleOwner,
            { movieWatchlistItem ->
                movieWatchlistRecyclerView.adapter =
                    MovieWatchlistAdapter(movieWatchlistItem, findNavController())
            })

        movieWatchlistButton.setOnClickListener {
            movieWatchlistViewModel.getMovieWatchlist(accountId, sessionId)

            movieWatchlistViewModel.movieWatchlistItemLiveData.observe(
                viewLifecycleOwner,
                { movieWatchlistItem ->
                    movieWatchlistRecyclerView.adapter =
                        MovieWatchlistAdapter(movieWatchlistItem, findNavController())
                })
        }

        tvWatchlistButton.setOnClickListener {
            tvWatchlistViewModel.getTvWatchlist(accountId, sessionId)

            tvWatchlistViewModel.tvWatchlistItemLiveData.observe(
                viewLifecycleOwner,
                { tvWatchlistItem ->
                    movieWatchlistRecyclerView.adapter =
                        TvWatchlistAdapter(tvWatchlistItem, findNavController())
                })
        }
    }
}