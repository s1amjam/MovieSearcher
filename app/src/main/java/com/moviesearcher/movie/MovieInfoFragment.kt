package com.moviesearcher.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.moviesearcher.R
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.databinding.FragmentMovieInfoBinding
import com.moviesearcher.list.lists.viewmodel.MyListsViewModel
import com.moviesearcher.movie.viewmodel.MovieInfoViewModel
import com.moviesearcher.utils.Constants

private const val TAG = "MovieInfoFragment"

class MovieInfoFragment : BaseFragment() {
    private var _binding: FragmentMovieInfoBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<MovieInfoFragmentArgs>()
    private val movieInfoViewModel: MovieInfoViewModel by viewModels()
    private val myLists: MyListsViewModel by viewModels()

    private lateinit var movieInfoPosterImageView: ImageView
    private lateinit var movieInfoTitle: TextView
    private lateinit var movieInfoGenres: TextView
    private lateinit var movieInfoProductionCountries: TextView
    private lateinit var movieInfoRuntime: TextView
    private lateinit var movieInfoTagline: TextView
    private lateinit var movieInfoReleaseDate: TextView
    private lateinit var movieInfoOverview: TextView
    private lateinit var movieInfoConstraintLayout: ConstraintLayout
    private lateinit var menuButtonAddToList: Button
    private lateinit var buttonMarkMovieAsFavorite: Button
    private lateinit var buttonWatchlist: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieInfoBinding.inflate(inflater, container, false)
        val view = binding.root
        val movieId = args.movieId

        movieInfoConstraintLayout = binding.movieInfoConstraintLayout
        movieInfoPosterImageView = binding.movieInfoPosterImageView
        movieInfoTitle = binding.movieInfoTitle
        movieInfoGenres = binding.movieInfoGenres
        movieInfoProductionCountries = binding.movieInfoProductionCountries
        movieInfoRuntime = binding.movieInfoRuntime
        movieInfoTagline = binding.movieInfoTagline
        movieInfoReleaseDate = binding.movieInfoReleaseDate
        movieInfoOverview = binding.movieInfoOverview
        menuButtonAddToList = binding.menuButtonAddMovieToList
        buttonMarkMovieAsFavorite = binding.buttonMarkMovieAsFavorite
        buttonWatchlist = binding.buttonWatchlist

        menuButtonAddToList.isVisible = sessionId != ""
        buttonMarkMovieAsFavorite.isVisible = sessionId != ""
        buttonWatchlist.isVisible = sessionId != ""

        movieInfoViewModel.getMovieInfoById(movieId).observe(
            viewLifecycleOwner,
            { movieInfo ->
                Glide.with(this)
                    .load(Constants.IMAGE_URL + movieInfo?.posterPath)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .into(movieInfoPosterImageView)
                movieInfoTitle.text = movieInfo?.title
                movieInfoGenres.text =
                    movieInfo?.genres?.joinToString { genre -> genre.name!! }
                movieInfoProductionCountries.text =
                    movieInfo?.productionCountries?.joinToString { productionCountry ->
                        productionCountry.name!!
                    }
                movieInfoRuntime.text = movieInfo?.runtime.toString() //TODO: format
                movieInfoTagline.text = movieInfo?.tagline
                movieInfoReleaseDate.text = movieInfo?.releaseDate
                movieInfoOverview.text = movieInfo?.overview
            })

        menuButtonAddToList.setOnClickListener { v ->
            myLists.getLists(accountId, sessionId, 1).observe(viewLifecycleOwner, {
                showAddToListMenu(v, R.menu.list_popup_menu, it.results!!)
            })
        }

        checkFavorites(buttonMarkMovieAsFavorite)

        buttonMarkMovieAsFavorite.setOnClickListener {
            markAsFavorite(buttonMarkMovieAsFavorite)
        }

        //checkWatchlist(buttonWatchlist)

        buttonWatchlist.setOnClickListener {
            //addToWatchlist(buttonWatchlist)
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}