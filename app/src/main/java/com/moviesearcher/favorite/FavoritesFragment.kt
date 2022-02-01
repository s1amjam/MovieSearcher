package com.moviesearcher.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.common.viewmodel.BaseViewModel
import com.moviesearcher.databinding.FragmentFavoritesBinding
import com.moviesearcher.favorite.movie.adapter.FavoriteMovieAdapter
import com.moviesearcher.favorite.movie.model.FavoriteMovieResponse
import com.moviesearcher.favorite.tv.adapter.FavoriteTvAdapter

private const val TAG = "FavoritesFragment"

class FavoritesFragment : BaseFragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteMoviesRecyclerView: RecyclerView
    private val viewModel: BaseViewModel by viewModels()

    private lateinit var favoriteMoviesButton: Button
    private lateinit var favoriteTvsButton: Button
    private lateinit var progressBar: ProgressBar

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
        progressBar.visibility = View.VISIBLE
        favoriteMoviesRecyclerView.visibility = View.INVISIBLE

        getFavoriteMovies()

        favoriteMoviesButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            favoriteMoviesRecyclerView.visibility = View.INVISIBLE

            getFavoriteMovies()
        }

        favoriteTvsButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            favoriteMoviesRecyclerView.visibility = View.INVISIBLE

            viewModel.getFavoriteTvs(accountId, sessionId).observe(
                viewLifecycleOwner
            ) { favoriteTvItems ->
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

                progressBar.visibility = View.GONE
                favoriteMoviesRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun getFavoriteMovies() {
        viewModel.getFavoriteMovies(accountId, sessionId).observe(
            viewLifecycleOwner
        ) { favoriteMovieItems ->
            setupFavoriteMovieUi(favoriteMovieItems)

            progressBar.visibility = View.GONE
            favoriteMoviesRecyclerView.visibility = View.VISIBLE
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}