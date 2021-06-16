package com.moviesearcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.adapters.MovieAdapter
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.auth.SessionId
import com.moviesearcher.utils.EncryptedSharedPrefs
import com.moviesearcher.viewmodel.MovieViewModel
import com.moviesearcher.viewmodel.TvViewModel

private const val TAG = "MovieSearcherFragment"

//TODO: loading indicator
class MovieSearcherFragment : Fragment() {
    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var tvViewModel: TvViewModel
    private lateinit var trendingMovieButton: Button
    private lateinit var trendingTvButton: Button
    private lateinit var sessionId: String
    private lateinit var authorizeButton: MenuItem
    private lateinit var logoutButton: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_movie_searcher_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView
        authorizeButton = menu.findItem(R.id.login_button)
        logoutButton = menu.findItem(R.id.logout_button)
        sessionId = EncryptedSharedPrefs.sharedPrefs(requireContext())
            .getString("sessionId", "").toString()

        authorizeButton.isVisible = sessionId == ""
        logoutButton.isVisible = !authorizeButton.isVisible

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(searchQuery: String): Boolean {
                    navigateToSearchResult(searchQuery)
                    return false
                }

                override fun onQueryTextChange(queryText: String): Boolean {
                    return false
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.login_button -> {
                AuthorizationDialogFragment().show(
                    activity?.supportFragmentManager!!, AuthorizationDialogFragment.TAG
                )
                true
            }
            R.id.logout_button -> {
                Api.deleteSession(SessionId(sessionId)).observe(requireActivity(),
                    { response ->
                        if (response.success == true) {
                            with(EncryptedSharedPrefs.sharedPrefs(requireContext()).edit()) {
                                remove("sessionId").apply()
                            }
                            activity?.invalidateOptionsMenu()
                        }
                    })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_searcher, container, false)
        movieRecyclerView = view.findViewById(R.id.movie_recycler_view)
        trendingMovieButton = view.findViewById(R.id.trending_movie_button)
        trendingTvButton = view.findViewById(R.id.trending_tv_button)
        movieRecyclerView.layoutManager = GridLayoutManager(context, 3)
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        //TODO: if we are going back from tv info, movies showing instead of tv
        trendingTvButton.setOnClickListener {
            tvViewModel = ViewModelProvider(this).get(TvViewModel::class.java)

            tvViewModel.tvItemLiveData.observe(
                viewLifecycleOwner,
                { movieItems ->
                    movieRecyclerView.adapter = MovieAdapter(movieItems, findNavController())
                })
        }

        trendingMovieButton.setOnClickListener {
            movieViewModel.movieItemLiveData.observe(
                viewLifecycleOwner,
                { movieItems ->
                    movieRecyclerView.adapter = MovieAdapter(movieItems, findNavController())
                })
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel.movieItemLiveData.observe(
            viewLifecycleOwner,
            { movieItems ->
                movieRecyclerView.adapter = MovieAdapter(movieItems, findNavController())
            })

        movieRecyclerView.addItemDecoration(
            MovieAdapter.GridSpacingItemDecoration(
                3,
                5,
                true
            )
        )
    }

    private fun navigateToSearchResult(searchQuery: String) {
        val action =
            MovieSearcherFragmentDirections.actionMovieSearcherFragmentToSearchResultFragment(
                searchQuery
            )
        findNavController().navigate(action)
    }
}