package com.moviesearcher.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.common.utils.OnClickListener
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
    private lateinit var watchlistRv: RecyclerView
    private lateinit var movieWatchlistButton: Button
    private lateinit var tvWatchlistButton: Button
    private lateinit var progressBar: ProgressBar

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

        navController = findNavController()
        watchlistRv = binding.watchlistRv
        movieWatchlistButton = binding.moviesWatchlistButton
        tvWatchlistButton = binding.tvWatchlistButton
        progressBar = binding.watchlistPb

        setupViewModel()
        setupMovieUi()

        movieWatchlistButton.setOnClickListener {
            setupMovieUi()
        }

        tvWatchlistButton.setOnClickListener {
            setupTvUi()
        }
    }

    private fun setupMovieUi() {
        viewModel.getMovieWatchlist().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { movieItems ->
                        movieItems.results?.forEach { it -> movieWatchlistIds.add(it.id!!.toLong()) }
                        createWatchlistAdapter(movieItems)

                        progressBar.visibility = View.GONE
                        watchlistRv.visibility = View.VISIBLE
                    }
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    watchlistRv.visibility = View.GONE
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

    private fun setupTvUi() {
        viewModel.getTvWatchlist().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { tvItems ->
                        tvItems.results?.forEach { it -> tvWatchlistIds.add(it.id!!.toLong()) }
                        createTvAdapter(tvItems)

                        progressBar.visibility = View.GONE
                        watchlistRv.visibility = View.VISIBLE
                    }
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    watchlistRv.visibility = View.GONE
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

    private fun createWatchlistAdapter(
        movieItems: MovieWatchlistResponse
    ): MovieWatchlistAdapter {
        val movieWatchlistAdapter = MovieWatchlistAdapter(
            movieItems,
            navController,
            OnClickListener { button: ImageButton, mediaInfo: MutableMap<String, Long>? ->
                addToWatchlist(button, mediaInfo)
            }
        )
        watchlistRv.apply {
            watchlistRv.adapter = movieWatchlistAdapter
            layoutManager = LinearLayoutManager(context)
        }

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
        watchlistRv.apply {
            watchlistRv.adapter = tvWatchlistAdapter
            layoutManager = LinearLayoutManager(context)
        }

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
        _binding?.watchlistRv?.adapter = null
        _binding = null
    }
}