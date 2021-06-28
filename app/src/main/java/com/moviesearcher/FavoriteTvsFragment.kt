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
import com.moviesearcher.adapters.FavoriteTvsAdapter
import com.moviesearcher.utils.EncryptedSharedPrefs
import com.moviesearcher.viewmodel.FavoriteTvsViewModel

private const val TAG = "FavoriteTvsFragment"

class FavoriteTvsFragment : Fragment() {
    private lateinit var favoriteTvsRecyclerView: RecyclerView
    private lateinit var favoriteTvsViewModel: FavoriteTvsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite_tvs, container, false)

        favoriteTvsRecyclerView = view.findViewById(R.id.fragment_favorite_tvs_recycler_view)
        favoriteTvsRecyclerView.layoutManager = LinearLayoutManager(context)
        favoriteTvsViewModel = ViewModelProvider(this).get(FavoriteTvsViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val encryptedSharedPrefs = EncryptedSharedPrefs.sharedPrefs(requireContext())
        val accountId: Int = encryptedSharedPrefs.getString("id", null)!!.toInt()
        val sessionId: String = encryptedSharedPrefs.getString("sessionId", null)!!

        favoriteTvsViewModel.getFavoriteTvs(accountId, sessionId)

        favoriteTvsViewModel.favoriteTvsItemLiveData.observe(
            viewLifecycleOwner,
            { favoriteMovieItems ->
                favoriteTvsRecyclerView.adapter =
                    FavoriteTvsAdapter(favoriteMovieItems, findNavController())
            })
    }
}