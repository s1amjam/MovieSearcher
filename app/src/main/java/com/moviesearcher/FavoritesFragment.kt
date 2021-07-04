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
import com.moviesearcher.adapters.FavoriteMoviesAdapter
import com.moviesearcher.adapters.FavoriteTvsAdapter
import com.moviesearcher.utils.EncryptedSharedPrefs
import com.moviesearcher.viewmodel.FavoriteMoviesViewModel
import com.moviesearcher.viewmodel.FavoriteTvsViewModel

private const val TAG = "FavoritesFragment"

class FavoritesFragment : Fragment() {
    private lateinit var favoriteMoviesRecyclerView: RecyclerView
    private lateinit var favoriteMoviesViewModel: FavoriteMoviesViewModel
    private lateinit var favoriteMoviesButton: Button
    private lateinit var favoriteTvsButton: Button
    private lateinit var favoriteTvsViewModel: FavoriteTvsViewModel

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
        favoriteMoviesViewModel = ViewModelProvider(this)
            .get(FavoriteMoviesViewModel::class.java)
        favoriteMoviesButton = view.findViewById(R.id.button_favorite_movies)
        favoriteTvsButton = view.findViewById(R.id.button_favorite_tvs)
        favoriteTvsViewModel = ViewModelProvider(this).get(FavoriteTvsViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val encryptedSharedPrefs = EncryptedSharedPrefs.sharedPrefs(requireContext())
        val accountId: Int = encryptedSharedPrefs.getString("accountId", null)!!.toInt()
        val sessionId: String = encryptedSharedPrefs.getString("sessionId", null)!!

        favoriteMoviesViewModel.getFavoriteMovies(accountId, sessionId)

        favoriteMoviesViewModel.favoriteMoviesItemLiveData.observe(
            viewLifecycleOwner,
            { favoriteMovieItems ->
                favoriteMoviesRecyclerView.adapter =
                    FavoriteMoviesAdapter(favoriteMovieItems, findNavController())
            })

        favoriteMoviesButton.setOnClickListener {
            favoriteMoviesViewModel.getFavoriteMovies(accountId, sessionId)

            favoriteMoviesViewModel.favoriteMoviesItemLiveData.observe(
                viewLifecycleOwner,
                { favoriteMovieItems ->
                    favoriteMoviesRecyclerView.adapter =
                        FavoriteMoviesAdapter(favoriteMovieItems, findNavController())
                })
        }

        favoriteTvsButton.setOnClickListener {
            favoriteTvsViewModel.getFavoriteTvs(accountId, sessionId)

            favoriteTvsViewModel.favoriteTvsItemLiveData.observe(
                viewLifecycleOwner,
                { favoriteTvItems ->
                    favoriteMoviesRecyclerView.adapter =
                        FavoriteTvsAdapter(favoriteTvItems, findNavController())
                })
        }
    }
}