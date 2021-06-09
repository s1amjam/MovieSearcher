package com.moviesearcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.moviesearcher.utils.Constants
import com.moviesearcher.viewmodel.MovieInfoViewModel
import com.squareup.picasso.Picasso

private const val TAG = "MovieInfoFragment"

class MovieInfoFragment : Fragment() {
    private val args by navArgs<MovieInfoFragmentArgs>()
    private lateinit var movieInfoViewModel: MovieInfoViewModel
    private lateinit var movieInfoPosterImageView: ImageView
    private lateinit var movieInfoTitle: TextView
    private lateinit var movieInfoGenres: TextView
    private lateinit var movieInfoProductionCountries: TextView
    private lateinit var movieInfoRuntime: TextView
    private lateinit var movieInfoTagline: TextView
    private lateinit var movieInfoReleaseDate: TextView
    private lateinit var movieInfoOverview: TextView
    private lateinit var movieInfoConstraintLayout: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_info, container, false)
        movieInfoConstraintLayout = view.findViewById(R.id.movie_info_constraint_layout)
        movieInfoViewModel = MovieInfoViewModel()
        movieInfoPosterImageView = view.findViewById(R.id.movie_info_poster_image_view)
        movieInfoTitle = view.findViewById(R.id.movie_info_title)
        movieInfoGenres = view.findViewById(R.id.movie_info_genres)
        movieInfoProductionCountries = view.findViewById(R.id.movie_info_production_countries)
        movieInfoRuntime = view.findViewById(R.id.movie_info_runtime)
        movieInfoTagline = view.findViewById(R.id.movie_info_tagline)
        movieInfoReleaseDate = view.findViewById(R.id.movie_info_release_date)
        movieInfoOverview = view.findViewById(R.id.movie_info_overview)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = args.movieId
        movieInfoViewModel.getMovieInfoById(movieId)

        movieInfoViewModel.movieInfoLiveData.observe(
            viewLifecycleOwner,
            { movieInfo ->
                Picasso.get()
                    .load(Constants.IMAGE_URL + movieInfo?.posterPath)
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
    }
}