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
import com.squareup.picasso.Picasso

private const val TAG = "MovieInfoFragment"

class MovieInfoFragment : Fragment() {
    private val args by navArgs<MovieInfoFragmentArgs>()
    lateinit var movieDetailsViewModel: MovieDetailsViewModel
    private lateinit var movieDetailsPosterImageView: ImageView
    private lateinit var movieDetailsTitle: TextView
    private lateinit var movieDetailsGenres: TextView
    private lateinit var movieDetailsProductionCountries: TextView
    private lateinit var movieDetailsRuntime: TextView
    private lateinit var movieDetailsTagline: TextView
    private lateinit var movieDetailsReleaseDate: TextView
    private lateinit var movieDetailsOverview: TextView
    private lateinit var movieDetailsConstraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_info, container, false)
        val movieId = args.movieId
        movieDetailsConstraintLayout = view.findViewById(R.id.movie_details_constraint_layout)
        movieDetailsViewModel = MovieDetailsViewModel(movieId)
        movieDetailsPosterImageView = view.findViewById(R.id.movie_details_poster_image_view)
        movieDetailsTitle = view.findViewById(R.id.movie_details_title)
        movieDetailsGenres = view.findViewById(R.id.movie_details_genres)
        movieDetailsProductionCountries = view.findViewById(R.id.movie_details_production_countries)
        movieDetailsRuntime = view.findViewById(R.id.movie_details_runtime)
        movieDetailsTagline = view.findViewById(R.id.movie_details_tagline)
        movieDetailsReleaseDate = view.findViewById(R.id.movie_details_release_date)
        movieDetailsOverview = view.findViewById(R.id.movie_details_overview)
        val movieDetailsResponse = movieDetailsViewModel.movieDetailsLiveData.value

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieDetailsViewModel.movieDetailsLiveData.observe(
            viewLifecycleOwner,
            { movieDetails ->
                Picasso.get()
                    .load(Constants.IMAGE_URL + movieDetails?.posterPath)
                    .into(movieDetailsPosterImageView)
                movieDetailsTitle.text = movieDetails?.title
                movieDetailsGenres.text = movieDetails?.genres?.joinToString(", ")
                movieDetailsProductionCountries.text = movieDetails?.productionCountries?.joinToString(", ")
                movieDetailsRuntime.text = movieDetails?.runtime.toString() //TODO: format
                movieDetailsTagline.text = movieDetails?.tagline
                movieDetailsReleaseDate.text = movieDetails?.releaseDate
                movieDetailsOverview.text = movieDetails?.overview
            })
    }
}