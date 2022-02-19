package com.moviesearcher.rated

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.common.utils.Status
import com.moviesearcher.common.viewmodel.ViewModelFactory
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
    private lateinit var viewModel: RatedViewModel

    private lateinit var ratedMoviesButton: Button
    private lateinit var ratedTvsButton: Button
    private lateinit var ratedTvEpisodesButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRatedBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ratedMoviesRecyclerView = binding.fragmentRatedRecyclerView
        ratedMoviesButton = binding.buttonRatedMovies
        ratedTvsButton = binding.buttonRatedTvs
        ratedTvEpisodesButton = binding.buttonRatedTvEpisodes

        setupViewModel()

        viewModel.getRatedMovies().observe(
            viewLifecycleOwner
        ) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { ratedMovieItems ->
                        setupRatedMoviesUi(ratedMovieItems)
                    }
                }
                Status.LOADING -> {
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

        ratedMoviesButton.setOnClickListener {
            viewModel.getRatedMovies().observe(
                viewLifecycleOwner
            ) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { ratedMovieItems ->
                            setupRatedMoviesUi(ratedMovieItems)
                        }
                    }
                    Status.LOADING -> {
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

        ratedTvsButton.setOnClickListener {
            viewModel.getRatedTvs().observe(
                viewLifecycleOwner
            ) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { ratedTvItems ->
                            val ratedMoviesAdapter =
                                RatedTvsAdapter(ratedTvItems, findNavController())

                            ratedMoviesRecyclerView.apply {
                                adapter = ratedMoviesAdapter
                                layoutManager = LinearLayoutManager(context)
                            }
                            ratedMoviesAdapter.differ.submitList(ratedTvItems.results)
                        }
                    }
                    Status.LOADING -> {
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

        ratedTvEpisodesButton.setOnClickListener {
            viewModel.getRatedTvEpisodes().observe(
                viewLifecycleOwner
            ) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { ratedTvItems ->
                            ratedMoviesRecyclerView.adapter =
                                RatedTvEpisodesAdapter(ratedTvItems, findNavController())
                        }
                    }
                    Status.LOADING -> {
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
    }

    private fun setupRatedMoviesUi(ratedMovieItems: RatedMoviesResponse) {
        val ratedMoviesAdapter = RatedMoviesAdapter(ratedMovieItems, findNavController())

        ratedMoviesRecyclerView.apply {
            adapter = ratedMoviesAdapter
            layoutManager = LinearLayoutManager(context)
        }
        ratedMoviesAdapter.differ.submitList(ratedMovieItems.results)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory(
                sessionId, accountId
            )
        ).get(RatedViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}