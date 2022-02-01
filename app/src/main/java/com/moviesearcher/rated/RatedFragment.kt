package com.moviesearcher.rated

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
import com.moviesearcher.common.viewmodel.BaseViewModel
import com.moviesearcher.databinding.FragmentRatedBinding
import com.moviesearcher.rated.movie.adapter.RatedMoviesAdapter
import com.moviesearcher.rated.movie.model.RatedMoviesResponse
import com.moviesearcher.rated.tv.adapter.RatedTvsAdapter
import com.moviesearcher.rated.tvepisode.adapter.RatedTvEpisodesAdapter

private const val TAG = "RatedFragment"

class RatedFragment : BaseFragment() {
    private var _binding: FragmentRatedBinding? = null
    private val binding get() = _binding!!

    private lateinit var ratedMoviesRecyclerView: RecyclerView
    private val viewModel: BaseViewModel by viewModels()

    private lateinit var ratedMoviesButton: Button
    private lateinit var ratedTvsButton: Button
    private lateinit var ratedTvEpisodesButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRatedBinding.inflate(inflater, container, false)
        val view = binding.root

        ratedMoviesRecyclerView = binding.fragmentRatedRecyclerView
        ratedMoviesButton = binding.buttonRatedMovies
        ratedTvsButton = binding.buttonRatedTvs
        ratedTvEpisodesButton = binding.buttonRatedTvEpisodes

        viewModel.getRatedMovies(accountId, sessionId).observe(
            viewLifecycleOwner
        ) { ratedMovieItems ->
            setupRatedMoviesUi(ratedMovieItems)
        }

        ratedMoviesButton.setOnClickListener {
            viewModel.getRatedMovies(accountId, sessionId).observe(
                viewLifecycleOwner
            ) { ratedMovieItems ->
                setupRatedMoviesUi(ratedMovieItems)
            }
        }

        ratedTvsButton.setOnClickListener {
            viewModel.getRatedTvs(accountId, sessionId).observe(
                viewLifecycleOwner
            ) { ratedTvItems ->
                val ratedMoviesAdapter = RatedTvsAdapter(ratedTvItems, findNavController())

                ratedMoviesRecyclerView.apply {
                    adapter = ratedMoviesAdapter
                    layoutManager = LinearLayoutManager(context)
                }
                ratedMoviesAdapter.differ.submitList(ratedTvItems.results)
            }
        }

        ratedTvEpisodesButton.setOnClickListener {
            viewModel.getRatedTvEpisodes(accountId, sessionId).observe(
                viewLifecycleOwner
            ) { ratedTvItems ->
                ratedMoviesRecyclerView.adapter =
                    RatedTvEpisodesAdapter(ratedTvItems, findNavController())
            }
        }

        return view
    }

    private fun setupRatedMoviesUi(ratedMovieItems: RatedMoviesResponse) {
        val ratedMoviesAdapter = RatedMoviesAdapter(ratedMovieItems, findNavController())

        ratedMoviesRecyclerView.apply {
            adapter = ratedMoviesAdapter
            layoutManager = LinearLayoutManager(context)
        }
        ratedMoviesAdapter.differ.submitList(ratedMovieItems.results)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}