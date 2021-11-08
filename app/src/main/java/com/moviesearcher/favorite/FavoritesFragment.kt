package com.moviesearcher.favorite

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
import com.moviesearcher.databinding.FragmentFavoritesBinding
import com.moviesearcher.favorite.movie.adapter.FavoriteMovieAdapter
import com.moviesearcher.favorite.movie.model.FavoriteMovieResponse
import com.moviesearcher.favorite.movie.viewmodel.FavoriteMoviesViewModel
import com.moviesearcher.favorite.tv.adapter.FavoriteTvAdapter
import com.moviesearcher.favorite.tv.viewmodel.FavoriteTvsViewModel

private const val TAG = "FavoritesFragment"

class FavoritesFragment : BaseFragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteMoviesRecyclerView: RecyclerView
    private val favoriteMoviesViewModel: FavoriteMoviesViewModel by viewModels()
    private val favoriteTvsViewModel: FavoriteTvsViewModel by viewModels()

    private lateinit var favoriteMoviesButton: Button
    private lateinit var favoriteTvsButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root

        favoriteMoviesRecyclerView = binding.fragmentFavoritesRecyclerView
        favoriteMoviesButton = binding.buttonFavoriteMovies
        favoriteTvsButton = binding.buttonFavoriteTvs

        favoriteMoviesViewModel.getFavoriteMovies(accountId, sessionId).observe(
            viewLifecycleOwner,
            { favoriteMovieItems ->
                setupFavoriteMovieUi(favoriteMovieItems)
            })

        favoriteMoviesButton.setOnClickListener {
            favoriteMoviesViewModel.getFavoriteMovies(accountId, sessionId).observe(
                viewLifecycleOwner,
                { favoriteMovieItems ->
                    setupFavoriteMovieUi(favoriteMovieItems)
                })
        }

        favoriteTvsButton.setOnClickListener {
            favoriteTvsViewModel.getFavoriteTvs(accountId, sessionId).observe(
                viewLifecycleOwner,
                { favoriteTvItems ->
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
                })
        }

        return view
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