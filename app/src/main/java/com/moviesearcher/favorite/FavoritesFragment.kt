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
import com.moviesearcher.favorite.movie.adapter.FavoriteTvAdapter
import com.moviesearcher.favorite.movie.viewmodel.FavoriteMoviesViewModel
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
        favoriteMoviesRecyclerView.layoutManager = LinearLayoutManager(context)
        favoriteMoviesButton = binding.buttonFavoriteMovies
        favoriteTvsButton = binding.buttonFavoriteTvs

        favoriteMoviesViewModel.getFavoriteMovies(accountId, sessionId).observe(
            viewLifecycleOwner,
            { favoriteMovieItems ->
                favoriteMoviesRecyclerView.adapter =
                    FavoriteMovieAdapter(
                        favoriteMovieItems,
                        findNavController(),
                        sessionId,
                        accountId
                    )
            })

        favoriteMoviesButton.setOnClickListener {
            favoriteMoviesViewModel.getFavoriteMovies(accountId, sessionId).observe(
                viewLifecycleOwner,
                { favoriteMovieItems ->
                    favoriteMoviesRecyclerView.adapter =
                        FavoriteMovieAdapter(
                            favoriteMovieItems,
                            findNavController(),
                            sessionId,
                            accountId
                        )
                })
        }

        favoriteTvsButton.setOnClickListener {
            favoriteTvsViewModel.getFavoriteTvs(accountId, sessionId).observe(
                viewLifecycleOwner,
                { favoriteTvItems ->
                    favoriteMoviesRecyclerView.adapter =
                        FavoriteTvAdapter(
                            favoriteTvItems, findNavController(),
                            sessionId,
                            accountId
                        )
                })
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}