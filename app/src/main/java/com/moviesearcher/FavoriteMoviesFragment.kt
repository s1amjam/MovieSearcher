package com.moviesearcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.adapters.FavoriteMoviesAdapter
import com.moviesearcher.utils.EncryptedSharedPrefs
import com.moviesearcher.viewmodel.FavoriteMoviesViewModel

private const val TAG = "FavoriteMoviesFragment"

class FavoriteMoviesFragment : Fragment() {
    private lateinit var favoriteMoviesRecyclerView: RecyclerView
    private lateinit var favoriteMoviesViewModel: FavoriteMoviesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_favorite_movies, container, false
        )

        favoriteMoviesRecyclerView = view.findViewById(R.id.fragment_favorite_movies_recycler_view)
        favoriteMoviesRecyclerView.layoutManager = LinearLayoutManager(context)
        favoriteMoviesViewModel = ViewModelProvider(this)
            .get(FavoriteMoviesViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val encryptedSharedPrefs = EncryptedSharedPrefs.sharedPrefs(requireContext())
        val accountId: Int = encryptedSharedPrefs.getString("id", null)!!.toInt()
        val sessionId: String = encryptedSharedPrefs.getString("sessionId", null)!!

        favoriteMoviesViewModel.getFavoriteMovies(accountId, sessionId)

        favoriteMoviesViewModel.favoriteMoviesItemLiveData.observe(
            viewLifecycleOwner,
            { favoriteMovieItems ->
                favoriteMoviesRecyclerView.adapter =
                    FavoriteMoviesAdapter(favoriteMovieItems, findNavController())
            })
    }
}