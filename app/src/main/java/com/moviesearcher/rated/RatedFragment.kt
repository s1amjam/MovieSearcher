package com.moviesearcher.rated

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.R
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.rated.movie.adapter.RatedMoviesAdapter
import com.moviesearcher.rated.movie.viewmodel.RatedMoviesViewModel
import com.moviesearcher.rated.tv.adapter.RatedTvsAdapter
import com.moviesearcher.rated.tv.viewmodel.RatedTvsViewModel
import com.moviesearcher.rated.tvepisode.adapter.RatedTvEpisodesAdapter
import com.moviesearcher.rated.tvepisode.viewmodel.RatedTvEpisodesViewModel

private const val TAG = "RatedFragment"

class RatedFragment : BaseFragment() {
    private lateinit var ratedMoviesRecyclerView: RecyclerView
    private val ratedMovies: RatedMoviesViewModel by viewModels()
    private val ratedTvEpisodes: RatedTvEpisodesViewModel by viewModels()
    private val ratedTvs: RatedTvsViewModel by viewModels()
    private lateinit var ratedMoviesButton: Button
    private lateinit var ratedTvsButton: Button
    private lateinit var ratedTvEpisodesButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rated, container, false)

        ratedMoviesRecyclerView = view.findViewById(R.id.fragment_rated_recycler_view)
        ratedMoviesRecyclerView.layoutManager = LinearLayoutManager(context)
        ratedMoviesButton = view.findViewById(R.id.button_rated_movies)
        ratedTvsButton = view.findViewById(R.id.button_rated_tvs)
        ratedTvEpisodesButton = view.findViewById(R.id.button_rated_tv_episodes)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ratedMovies.getRatedMovies(accountId, sessionId).observe(
            viewLifecycleOwner,
            { ratedMovieItems ->
                ratedMoviesRecyclerView.adapter =
                    RatedMoviesAdapter(ratedMovieItems, findNavController())
            })

        ratedMoviesButton.setOnClickListener {
            ratedMovies.getRatedMovies(accountId, sessionId).observe(
                viewLifecycleOwner,
                { ratedMovieItems ->
                    ratedMoviesRecyclerView.adapter =
                        RatedMoviesAdapter(ratedMovieItems, findNavController())
                })
        }

        ratedTvsButton.setOnClickListener {
            ratedTvs.getRatedTvs(accountId, sessionId).observe(
                viewLifecycleOwner,
                { ratedTvItems ->
                    ratedMoviesRecyclerView.adapter =
                        RatedTvsAdapter(ratedTvItems, findNavController())
                })
        }

        ratedTvEpisodesButton.setOnClickListener {
            ratedTvEpisodes.getRatedTvEpisodes(accountId, sessionId).observe(
                viewLifecycleOwner,
                { ratedTvItems ->
                    ratedMoviesRecyclerView.adapter =
                        RatedTvEpisodesAdapter(ratedTvItems, findNavController())
                })
        }
    }
}