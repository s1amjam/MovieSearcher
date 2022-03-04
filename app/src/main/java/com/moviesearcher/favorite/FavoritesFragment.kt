package com.moviesearcher.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.common.utils.Status
import com.moviesearcher.common.viewmodel.ViewModelFactory
import com.moviesearcher.databinding.FragmentFavoritesBinding
import com.moviesearcher.favorite.movie.adapter.FavoriteMovieAdapter
import com.moviesearcher.favorite.movie.model.FavoriteMovieResponse
import com.moviesearcher.favorite.tv.adapter.FavoriteTvAdapter
import com.moviesearcher.favorite.tv.model.FavoriteTvResponse

private const val TAG = "FavoritesFragment"

class FavoritesFragment : BaseFragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteMoviesRecyclerView: RecyclerView
    private lateinit var viewModel: FavoriteViewModel

    private lateinit var favoriteMoviesButton: Button
    private lateinit var favoriteTvsButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var dontHaveFavoritesTv: TextView

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
        dontHaveFavoritesTv = binding.dontHaveFavoritesTextView

        setupViewModel()
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
                            dontHaveFavoritesTv.visibility = View.VISIBLE
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
        ) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { favoriteMovieItems ->
                        if (favoriteMovieItems.totalResults == 0) {
                            dontHaveFavoritesTv.visibility = View.VISIBLE
                        } else {
                            setupFavoriteTvsUi(favoriteMovieItems)

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

    private fun setupFavoriteMovieUi(favoriteMovieItems: FavoriteMovieResponse) {
        val favoriteMovieAdapter = FavoriteMovieAdapter(
            favoriteMovieItems,
            findNavController(),
            sessionId,
            accountId
        )

        favoriteMoviesRecyclerView.apply {
            adapter = favoriteMovieAdapter
            layoutManager = LinearLayoutManager(context)
        }
        favoriteMovieAdapter.differ.submitList(favoriteMovieItems.results)
    }

    private fun setupFavoriteTvsUi(favoriteTvItems: FavoriteTvResponse) {
        val favoriteTvAdapter = FavoriteTvAdapter(
            favoriteTvItems, findNavController(),
            sessionId,
            accountId
        )

        favoriteMoviesRecyclerView.apply {
            adapter = favoriteTvAdapter
            layoutManager = LinearLayoutManager(context)
        }
        favoriteTvAdapter.differ.submitList(favoriteTvItems.results)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory(
                sessionId,
                accountId
            )
        ).get(FavoriteViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}