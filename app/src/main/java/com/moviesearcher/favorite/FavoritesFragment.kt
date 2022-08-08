package com.moviesearcher.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.utils.Constants.ERROR_MESSAGE
import com.moviesearcher.common.utils.Status
import com.moviesearcher.databinding.FragmentFavoritesBinding
import com.moviesearcher.favorite.model.MarkAsFavoriteRequest
import com.moviesearcher.favorite.movie.adapter.FavoriteMovieAdapter
import com.moviesearcher.favorite.movie.model.FavoriteMovieResponse
import com.moviesearcher.favorite.movie.model.ResultFavoriteMovie
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteMoviesRecyclerView: RecyclerView
    private val viewModel by viewModels<FavoriteViewModel>()

    private lateinit var favoriteMoviesButton: Button
    private lateinit var favoriteTvsButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var noFavoritesTv: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteMoviesRecyclerView = binding.fragmentFavoritesRecyclerView
        favoriteMoviesButton = binding.buttonFavoriteMovies
        favoriteTvsButton = binding.buttonFavoriteTvs
        progressBar = binding.progressBarFavorites
        noFavoritesTv = binding.dontHaveFavoritesTextView

        setupUi()
    }

    private fun setupUi() {
        getMovies()

        favoriteMoviesButton.setOnClickListener {
            getMovies()
        }

        favoriteTvsButton.setOnClickListener {
            getTvs()
        }
    }

    private fun getMovies() {
        viewModel.getFavoriteMovie().observe(
            viewLifecycleOwner
        ) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { favoriteMovieItems ->
                        if (favoriteMovieItems.totalResults == 0) {
                            noFavoritesTv.visibility = View.VISIBLE
                        } else {
                            setupFavoriteMovieUi(favoriteMovieItems)

                            progressBar.visibility = View.GONE
                            favoriteMoviesRecyclerView.visibility = View.VISIBLE
                        }
                        progressBar.visibility = View.GONE
                    }
                }
                Status.LOADING -> {
                    favoriteMoviesRecyclerView.visibility = View.GONE
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

    private fun getTvs() {
        viewModel.getFavoriteTv().observe(
            viewLifecycleOwner
        ) { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { favoriteTvItems ->

                        val favoriteTvsResponse = FavoriteMovieResponse().apply {
                            val list = mutableListOf<ResultFavoriteMovie>()
                            favoriteTvItems.results?.forEach {
                                list.add(
                                    ResultFavoriteMovie(
                                        id = it.id,
                                        title = it.name,
                                        overview = it.overview,
                                        voteAverage = it.voteAverage,
                                        releaseDate = it.firstAirDate,
                                        posterPath = it.posterPath
                                    )
                                )
                            }

                            results = list
                        }
                        setupFavoriteMovieUi(favoriteTvsResponse, true)

                        favoriteMoviesRecyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }
                }
                Status.LOADING -> {
                    favoriteMoviesRecyclerView.visibility = View.GONE
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

    private fun setupFavoriteMovieUi(
        favoriteMovieItems: FavoriteMovieResponse,
        isTv: Boolean = false
    ) {
        val favoriteMovieAdapter = FavoriteMovieAdapter(
            findNavController(),
            object : FavoriteMovieAdapter.ItemClickListener {
                override fun onItemClick(
                    button: ImageButton,
                    media: MutableMap<String, Long>,
                    context: Context,
                    lifecycleOwner: LifecycleOwner
                ) {
                    viewModel.postMarkAsFavorite(
                        MarkAsFavoriteRequest(
                            button.tag.toString().toBoolean(),
                            media.values.first(),
                            media.keys.first()
                        )
                    )

                    viewModel.processFavoriteButtons(button)
                }
            },
            isTv
        )

        favoriteMoviesRecyclerView.apply {
            adapter = favoriteMovieAdapter
            layoutManager = LinearLayoutManager(context)
        }
        favoriteMovieAdapter.submitList(favoriteMovieItems.results)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}