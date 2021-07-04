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
import com.moviesearcher.adapters.RatedMoviesAdapter
import com.moviesearcher.adapters.RatedTvEpisodesAdapter
import com.moviesearcher.adapters.RatedTvsAdapter
import com.moviesearcher.utils.EncryptedSharedPrefs
import com.moviesearcher.viewmodel.RatedMoviesViewModel
import com.moviesearcher.viewmodel.RatedTvEpisodesViewModel
import com.moviesearcher.viewmodel.RatedTvsViewModel

private const val TAG = "RatedFragment"

class RatedFragment : Fragment() {
    private lateinit var ratedMoviesRecyclerView: RecyclerView
    private lateinit var ratedMoviesViewModel: RatedMoviesViewModel
    private lateinit var ratedTvEpisodesViewModel: RatedTvEpisodesViewModel
    private lateinit var ratedTvsViewModel: RatedTvsViewModel
    private lateinit var ratedMoviesButton: Button
    private lateinit var ratedTvsButton: Button
    private lateinit var ratedTvEpisodesButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_rated, container, false
        )

        ratedMoviesRecyclerView = view.findViewById(R.id.fragment_rated_recycler_view)
        ratedMoviesRecyclerView.layoutManager = LinearLayoutManager(context)
        ratedMoviesViewModel = ViewModelProvider(this)
            .get(RatedMoviesViewModel::class.java)
        ratedTvEpisodesViewModel = ViewModelProvider(this)
            .get(RatedTvEpisodesViewModel::class.java)
        ratedTvsViewModel = ViewModelProvider(this).get(RatedTvsViewModel::class.java)
        ratedMoviesButton = view.findViewById(R.id.button_rated_movies)
        ratedTvsButton = view.findViewById(R.id.button_rated_tvs)
        ratedTvEpisodesButton = view.findViewById(R.id.button_rated_tv_episodes)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val encryptedSharedPrefs = EncryptedSharedPrefs.sharedPrefs(requireContext())
        val accountId: Int = encryptedSharedPrefs.getString("id", null)!!.toInt()
        val sessionId: String = encryptedSharedPrefs.getString("sessionId", null)!!

        ratedMoviesViewModel.getRatedMovies(accountId, sessionId)

        ratedMoviesViewModel.ratedMoviesItemLiveData.observe(
            viewLifecycleOwner,
            { ratedMovieItems ->
                ratedMoviesRecyclerView.adapter =
                    RatedMoviesAdapter(ratedMovieItems, findNavController())
            })

        ratedMoviesButton.setOnClickListener {
            ratedMoviesViewModel.getRatedMovies(accountId, sessionId)

            ratedMoviesViewModel.ratedMoviesItemLiveData.observe(
                viewLifecycleOwner,
                { ratedMovieItems ->
                    ratedMoviesRecyclerView.adapter =
                        RatedMoviesAdapter(ratedMovieItems, findNavController())
                })
        }

        ratedTvsButton.setOnClickListener {
            ratedTvsViewModel.getRatedTvs(accountId, sessionId)

            ratedTvsViewModel.ratedTvsItemLiveData.observe(
                viewLifecycleOwner,
                { ratedTvItems ->
                    ratedMoviesRecyclerView.adapter =
                        RatedTvsAdapter(ratedTvItems, findNavController())
                })
        }

        ratedTvEpisodesButton.setOnClickListener {
            ratedTvEpisodesViewModel.getRatedTvEpisodes(accountId, sessionId)

            ratedTvEpisodesViewModel.ratedTvEpisodesItemLiveData.observe(
                viewLifecycleOwner,
                { ratedTvItems ->
                    ratedMoviesRecyclerView.adapter =
                        RatedTvEpisodesAdapter(ratedTvItems, findNavController())
                })
        }
    }
}