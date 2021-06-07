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
import com.moviesearcher.api.entity.utils.Constants
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
        val movieId = args.movieId
        movieInfoConstraintLayout = view.findViewById(R.id.movie_details_constraint_layout)
        movieInfoViewModel = MovieInfoViewModel(movieId)
        movieInfoPosterImageView = view.findViewById(R.id.movie_details_poster_image_view)
        movieInfoTitle = view.findViewById(R.id.movie_details_title)
        movieInfoGenres = view.findViewById(R.id.movie_details_genres)
        movieInfoProductionCountries = view.findViewById(R.id.movie_details_production_countries)
        movieInfoRuntime = view.findViewById(R.id.movie_details_runtime)
        movieInfoTagline = view.findViewById(R.id.movie_details_tagline)
        movieInfoReleaseDate = view.findViewById(R.id.movie_details_release_date)
        movieInfoOverview = view.findViewById(R.id.movie_details_overview)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieInfoViewModel.movieInfoLiveData.observe(
            viewLifecycleOwner,
            { movieDetails ->
                Picasso.get()
                    .load(Constants.IMAGE_URL + movieDetails?.posterPath)
                    .into(movieInfoPosterImageView)
                movieInfoTitle.text = movieDetails?.title
                movieInfoGenres.text =
                    movieDetails?.genres?.joinToString { genre -> genre.name!! }
                movieInfoProductionCountries.text =
                    movieDetails?.productionCountries?.joinToString { productionCountry ->
                        productionCountry.name!!
                    }
                movieInfoRuntime.text = movieDetails?.runtime.toString() //TODO: format
                movieInfoTagline.text = movieDetails?.tagline
                movieInfoReleaseDate.text = movieDetails?.releaseDate
                movieInfoOverview.text = movieDetails?.overview
            })
    }
}