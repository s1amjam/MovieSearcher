package com.moviesearcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.adapters.FavoriteMoviesAdapter
import com.moviesearcher.adapters.FavoriteTvsAdapter
import com.moviesearcher.viewmodel.FavoriteMoviesViewModel
import com.moviesearcher.viewmodel.FavoriteTvsViewModel

private const val TAG = "FavoritesFragment"

class FavoritesFragment : BaseFragment() {
    private lateinit var favoriteMoviesRecyclerView: RecyclerView
    private val favoriteMoviesViewModel: FavoriteMoviesViewModel by viewModels()
    private lateinit var favoriteMoviesButton: Button
    private lateinit var favoriteTvsButton: Button
    private val favoriteTvsViewModel: FavoriteTvsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_favorites, container, false
        )

        favoriteMoviesRecyclerView = view.findViewById(R.id.fragment_favorites_recycler_view)
        favoriteMoviesRecyclerView.layoutManager = LinearLayoutManager(context)
        favoriteMoviesButton = view.findViewById(R.id.button_favorite_movies)
        favoriteTvsButton = view.findViewById(R.id.button_favorite_tvs)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteMoviesViewModel.getFavoriteMovies(accountId, sessionId).observe(
            viewLifecycleOwner,
            { favoriteMovieItems ->
                favoriteMoviesRecyclerView.adapter =
                    FavoriteMoviesAdapter(favoriteMovieItems, findNavController())
            })

        favoriteMoviesButton.setOnClickListener {
            favoriteMoviesViewModel.getFavoriteMovies(accountId, sessionId).observe(
                viewLifecycleOwner,
                { favoriteMovieItems ->
                    favoriteMoviesRecyclerView.adapter =
                        FavoriteMoviesAdapter(favoriteMovieItems, findNavController())
                })
        }

        favoriteTvsButton.setOnClickListener {
            favoriteTvsViewModel.getFavoriteTvs(accountId, sessionId).observe(
                viewLifecycleOwner,
                { favoriteTvItems ->
                    favoriteMoviesRecyclerView.adapter =
                        FavoriteTvsAdapter(favoriteTvItems, findNavController())
                })
        }
    }
}