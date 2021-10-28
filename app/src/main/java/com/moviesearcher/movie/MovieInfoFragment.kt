package com.moviesearcher.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moviesearcher.R
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.common.model.images.Backdrop
import com.moviesearcher.databinding.FragmentMovieInfoBinding
import com.moviesearcher.list.lists.viewmodel.MyListsViewModel
import com.moviesearcher.movie.adapter.cast.MovieCastAdapter
import com.moviesearcher.movie.adapter.images.ImagesAdapter
import com.moviesearcher.movie.adapter.recommendations.RecommendationsAdapter
import com.moviesearcher.movie.adapter.video.VideoAdapter
import com.moviesearcher.movie.viewmodel.MovieInfoViewModel
import com.moviesearcher.movie.viewmodel.cast.MovieCastViewModel
import com.moviesearcher.movie.viewmodel.images.ImagesViewModel
import com.moviesearcher.movie.viewmodel.recommendations.RecommendationsViewModel
import com.moviesearcher.movie.viewmodel.video.VideoViewModel
import com.moviesearcher.utils.Constants
import java.util.concurrent.TimeUnit

private const val TAG = "MovieInfoFragment"

class MovieInfoFragment : BaseFragment() {
    private var _binding: FragmentMovieInfoBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<MovieInfoFragmentArgs>()
    private val movieInfoViewModel: MovieInfoViewModel by viewModels()
    private val movieCastViewModel: MovieCastViewModel by viewModels()
    private val recommendationsViewModel: RecommendationsViewModel by viewModels()
    private val videoViewModel: VideoViewModel by viewModels()
    private val myLists: MyListsViewModel by viewModels()
    private val imagesViewModel: ImagesViewModel by viewModels()

    private lateinit var movieInfoCastRecyclerView: RecyclerView
    private lateinit var recommendationsRecyclerView: RecyclerView
    private lateinit var videoRecyclerView: RecyclerView
    private lateinit var imagesRecyclerView: RecyclerView
    private lateinit var movieInfoPosterImageView: ImageView
    private lateinit var movieInfoTitle: TextView
    private lateinit var movieInfoGenres: TextView
    private lateinit var movieInfoRuntime: TextView
    private lateinit var movieInfoTagline: TextView
    private lateinit var movieInfoReleaseDate: TextView
    private lateinit var movieInfoOverview: TextView
    private lateinit var voteAverage: TextView
    private lateinit var voteCount: TextView
    private lateinit var movieInfoConstraintLayout: ConstraintLayout
    private lateinit var menuButtonAddToList: Button
    private lateinit var buttonMarkMovieAsFavorite: Button
    private lateinit var buttonWatchlist: Button
    private lateinit var buttonSeeAllImages: Button
    private lateinit var rateButton: Button
    private lateinit var director: TextView
    private lateinit var writer: TextView
    private lateinit var videoCardView: CardView
    private lateinit var trailerPreview: ImageView
    private lateinit var trailerName: TextView
    private lateinit var trailerCardView: CardView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieInfoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = args.movieId

        movieInfoCastRecyclerView = binding.castRecyclerView
        movieInfoCastRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recommendationsRecyclerView = binding.recommendationsRecyclerView
        recommendationsRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        videoRecyclerView = binding.videoRecyclerView
        videoRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        imagesRecyclerView = binding.imagesRecyclerView
        imagesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        movieInfoConstraintLayout = binding.movieInfoConstraintLayout
        movieInfoPosterImageView = binding.movieInfoPosterImageView
        movieInfoTitle = binding.movieTitleTextView
        movieInfoGenres = binding.genresTextView
        movieInfoRuntime = binding.runtimeTextView
        movieInfoTagline = binding.taglineTextView
        movieInfoReleaseDate = binding.releaseDateTextView
        movieInfoOverview = binding.overviewTextView
        menuButtonAddToList = binding.menuButtonAddMovieToList
        buttonMarkMovieAsFavorite = binding.buttonMarkMovieAsFavorite
        buttonWatchlist = binding.buttonWatchlist
        buttonSeeAllImages = binding.buttonSeeAllImages
        voteAverage = binding.textViewRating
        voteCount = binding.textViewVoteCount
        rateButton = binding.rateButtonView
        director = binding.directorCastTextView
        writer = binding.writerCastTextView
        videoCardView = binding.videoCardView
        trailerPreview = binding.previewTrailerImageView
        trailerName = binding.trailerNameTextView
        trailerCardView = binding.trailerCardView

        menuButtonAddToList.isVisible = sessionId != ""
        buttonMarkMovieAsFavorite.isVisible = sessionId != ""
        buttonWatchlist.isVisible = sessionId != ""

        movieInfoViewModel.getMovieInfoById(movieId).observe(
            viewLifecycleOwner,
            { movieInfo ->
                val hours = TimeUnit.MINUTES.toHours(movieInfo?.runtime?.toLong()!!)
                val minutes = movieInfo.runtime.toLong() - TimeUnit.HOURS.toMinutes(hours)

                Glide.with(this)
                    .load(Constants.IMAGE_URL + movieInfo.posterPath)
                    .centerCrop()
                    .override(300, 500)
                    .into(movieInfoPosterImageView)
                movieInfoTitle.text = movieInfo.title
                movieInfoGenres.text =
                    movieInfo.genres?.joinToString { genre -> genre.name!! }
                movieInfoRuntime.text =
                    String.format("%02dh %02dm", hours, minutes).dropWhile { it == '0' }
                movieInfoTagline.text = movieInfo.tagline
                movieInfoReleaseDate.text = movieInfo.releaseDate?.dropLast(6)
                movieInfoOverview.text = movieInfo.overview
                voteAverage.text = getString(R.string.vote).format(movieInfo.voteAverage.toString())
                voteCount.text = movieInfo.voteCount.toString()

                movieInfoOverview.setOnClickListener {
                    MaterialAlertDialogBuilder(requireContext()).setMessage(movieInfo.overview)
                        .show()
                }
            })

        movieCastViewModel.getMovieCastById(movieId).observe(viewLifecycleOwner, { castItems ->
            movieInfoCastRecyclerView.adapter = MovieCastAdapter(
                castItems
            )
            director.text =
                getString(R.string.director).format(
                    castItems.crew?.find { it.job == "Director" }?.name
                )
            writer.text =
                getString(R.string.writer).format(
                    castItems.crew?.find { it.job == "Executive Producer" }?.name
                )
        })

        recommendationsViewModel.getRecommendationsByMovieId(movieId)
            .observe(viewLifecycleOwner, { recommendationsItems ->
                recommendationsRecyclerView.adapter = RecommendationsAdapter(
                    recommendationsItems,
                    findNavController()
                )
            })

        videoViewModel.getVideosByMovieId(movieId).observe(viewLifecycleOwner, { videoItems ->
            if (videoItems.results != null) {
                val officialTrailer = videoItems.results.find {
                    it.official == true && it.type == "Trailer"
                }
                if (officialTrailer != null) {
                    videoItems.results.remove(officialTrailer)

                    Glide.with(requireContext())
                        .load(Constants.YOUTUBE_PREVIEW_URL.format(officialTrailer.key))
                        .centerCrop()
                        .override(800, 600)
                        .into(trailerPreview)

                    trailerName.text = officialTrailer.name

                    trailerCardView.setOnClickListener {
                        findNavController().navigate(
                            MovieInfoFragmentDirections.actionMovieInfoFragmentToVideoFragment(
                                officialTrailer.key!!
                            )
                        )
                    }
                }

                videoRecyclerView.adapter = VideoAdapter(
                    videoItems,
                    findNavController()
                )
            } else {
                videoCardView.visibility = View.GONE
            }
        })

        imagesViewModel.getImagesByMovieId(movieId)
            .observe(viewLifecycleOwner, { imagesItems ->
                var tenImages = imagesItems.backdrops

                while (tenImages?.size!! > 10) {
                    tenImages = tenImages.dropLast(1) as MutableList<Backdrop>?
                }

                imagesItems.apply {
                    backdrops = tenImages
                }

                imagesRecyclerView.adapter = ImagesAdapter(
                    imagesItems,
                )
            })

        buttonSeeAllImages.setOnClickListener {
            findNavController().navigate(
                MovieInfoFragmentDirections.actionMovieInfoFragmentToImagesFragment(
                    movieId
                )
            )
        }

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

        rateButton.setOnClickListener { TODO() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}