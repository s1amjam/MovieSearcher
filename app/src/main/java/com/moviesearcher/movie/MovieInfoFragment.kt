package com.moviesearcher.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moviesearcher.R
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.common.model.images.Backdrop
import com.moviesearcher.common.viewmodel.cast.CastViewModel
import com.moviesearcher.common.viewmodel.images.ImagesViewModel
import com.moviesearcher.common.viewmodel.recommendations.RecommendationsViewModel
import com.moviesearcher.common.viewmodel.video.VideoViewModel
import com.moviesearcher.databinding.FragmentMovieInfoBinding
import com.moviesearcher.list.lists.model.ListsResponse
import com.moviesearcher.list.lists.viewmodel.MyListsViewModel
import com.moviesearcher.movie.adapter.cast.MovieCastAdapter
import com.moviesearcher.movie.adapter.images.ImagesAdapter
import com.moviesearcher.movie.adapter.recommendations.RecommendationsAdapter
import com.moviesearcher.movie.adapter.video.VideoAdapter
import com.moviesearcher.movie.model.cast.Cast
import com.moviesearcher.movie.viewmodel.MovieInfoViewModel
import com.moviesearcher.utils.Constants
import java.util.concurrent.TimeUnit

private const val TAG = "MovieInfoFragment"

class MovieInfoFragment : BaseFragment() {
    private var _binding: FragmentMovieInfoBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<MovieInfoFragmentArgs>()

    private val movieInfoViewModel: MovieInfoViewModel by viewModels()
    private val castViewModel: CastViewModel by viewModels()
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
    private lateinit var movieInfoRuntime: TextView
    private lateinit var movieInfoTagline: TextView
    private lateinit var movieInfoReleaseDate: TextView
    private lateinit var movieInfoOverview: TextView
    private lateinit var voteAverage: TextView
    private lateinit var voteCount: TextView
    private lateinit var movieInfoConstraintLayout: ConstraintLayout
    private lateinit var menuButtonAddToList: ImageButton
    private lateinit var buttonMarkMovieAsFavorite: ImageButton
    private lateinit var buttonWatchlist: ImageButton
    private lateinit var buttonSeeAllImages: Button
    private lateinit var rateButton: Button
    private lateinit var director: TextView
    private lateinit var writer: TextView
    private lateinit var videoCardView: CardView
    private lateinit var trailerPreview: ImageView
    private lateinit var trailerName: TextView
    private lateinit var trailerCardView: CardView
    private lateinit var releaseDate: TextView
    private lateinit var originCountry: TextView
    private lateinit var languageSpoken: TextView
    private lateinit var filmingLocations: TextView
    private lateinit var genresChipGroup: ChipGroup
    private lateinit var expandActivitiesButton: ImageButton
    private lateinit var activitiesConstraintLayout: ConstraintLayout
    private lateinit var mainCardView: CardView
    private lateinit var progressBar: ProgressBar
    private lateinit var recommendationsCardView: CardView

    private lateinit var lists: LiveData<ListsResponse>


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
        recommendationsRecyclerView = binding.recommendationsRecyclerView
        videoRecyclerView = binding.videoRecyclerView
        imagesRecyclerView = binding.imagesRecyclerView

        movieInfoConstraintLayout = binding.movieInfoConstraintLayout
        movieInfoConstraintLayout.visibility = View.INVISIBLE
        movieInfoPosterImageView = binding.movieInfoPosterImageView
        movieInfoTitle = binding.movieTitleTextView
        genresChipGroup = binding.chipGroupGenres
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
        releaseDate = binding.textviewReleaseDateDetail
        originCountry = binding.textviewOriginCountryDetail
        languageSpoken = binding.textviewLanguageSpokenDetail
        filmingLocations = binding.textviewFilmingLocationsDetail
        expandActivitiesButton = binding.expandActivitiesButton
        activitiesConstraintLayout = binding.activitiesConstraintLayout
        mainCardView = binding.mainMovieInfoCardView
        progressBar = binding.progressBarMovieInfo
        recommendationsCardView = binding.recommendationsCardView
        progressBar.visibility = View.VISIBLE

        menuButtonAddToList.isVisible = sessionId != ""
        buttonMarkMovieAsFavorite.isVisible = sessionId != ""
        buttonWatchlist.isVisible = sessionId != ""

        movieInfoViewModel.getMovieInfoById(movieId).observe(
            viewLifecycleOwner,
            { movieInfo ->
                val hours = TimeUnit.MINUTES.toHours(movieInfo?.runtime?.toLong()!!)
                val minutes = movieInfo.runtime.toLong() - TimeUnit.HOURS.toMinutes(hours)
                val languages = mutableListOf<String>()
                val locations = mutableListOf<String>()
                val genres = movieInfo.genres

                movieInfo.spokenLanguages?.forEach { languages.add(it.name!!) }
                movieInfo.productionCountries?.forEach { locations.add(it.name!!) }

                Glide.with(this)
                    .load(Constants.IMAGE_URL + movieInfo.posterPath)
                    .placeholder(R.drawable.ic_placeholder)
                    .centerCrop()
                    .override(300, 500)
                    .into(movieInfoPosterImageView)
                movieInfoTitle.text = movieInfo.title
                movieInfoRuntime.text =
                    String.format("%02dh %02dm", hours, minutes).dropWhile { it == '0' }
                movieInfoTagline.text = movieInfo.tagline
                movieInfoReleaseDate.text = movieInfo.releaseDate?.dropLast(6)
                movieInfoOverview.text = movieInfo.overview
                voteAverage.text = getString(R.string.vote).format(movieInfo.voteAverage.toString())
                voteCount.text = movieInfo.voteCount.toString()
                releaseDate.text = movieInfo.releaseDate?.replace("-", ".")
                originCountry.text = movieInfo.productionCountries?.get(0)?.name
                languageSpoken.text = languages.joinToString()
                filmingLocations.text = locations.joinToString()

                if (genres != null) {
                    for (genre in genres) {
                        val chip = this.layoutInflater.inflate(
                            R.layout.item_chip_tags,
                            null,
                            false
                        ) as Chip
                        chip.text = genre.name
                        genresChipGroup.addView(chip)
                    }
                }

                movieInfoOverview.setOnClickListener {
                    MaterialAlertDialogBuilder(requireContext()).setMessage(movieInfo.overview)
                        .show()
                }

                movieInfoConstraintLayout.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            })

        castViewModel.getMovieCastById(movieId).observe(viewLifecycleOwner, { castItems ->
            val movieCastAdapter = MovieCastAdapter(castItems, findNavController())
            var tenCast = castItems.cast

            while (tenCast?.size!! > 10) {
                tenCast = tenCast.dropLast(1) as MutableList<Cast>?
            }

            castItems.apply {
                cast = tenCast
            }

            movieInfoCastRecyclerView.apply {
                adapter = movieCastAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
            movieCastAdapter.differ.submitList(castItems.cast)

            director.text =
                getString(R.string.director).format(
                    castItems.crew?.find { it.job == "Director" }?.name
                )
            writer.text =
                getString(R.string.writer).format(
                    castItems.crew?.find { it.job == "Writing" }?.name
                )
        })

        recommendationsViewModel.getRecommendationsByMovieId(movieId)
            .observe(viewLifecycleOwner, { recommendationsItems ->
                if (recommendationsItems.totalResults == 0) {
                    recommendationsCardView.visibility = View.GONE
                } else {
                    val recommendationsAdapter = RecommendationsAdapter(
                        recommendationsItems,
                        findNavController()
                    )

                    recommendationsRecyclerView.apply {
                        adapter = recommendationsAdapter
                        layoutManager =
                            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    }
                    recommendationsAdapter.differ.submitList(recommendationsItems.results)
                }
            })

        videoViewModel.getVideosByMovieId(movieId).observe(viewLifecycleOwner, { videoItems ->
            val videoAdapter = VideoAdapter(
                videoItems,
                findNavController()
            )

            if (videoItems.results != null) {
                val officialTrailer = videoItems.results.find {
                    it.name?.contains(
                        "official trailer",
                        true
                    ) == true || it.name?.contains(
                        "trailer",
                        true
                    ) == true
                }
                if (officialTrailer != null) {
                    videoItems.results.remove(officialTrailer)

                    Glide.with(requireContext())
                        .load(Constants.YOUTUBE_PREVIEW_URL.format(officialTrailer.key))
                        .placeholder(R.drawable.ic_placeholder)
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

                videoRecyclerView.apply {
                    adapter = videoAdapter
                    layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                }
                videoAdapter.differ.submitList(videoItems.results)
            } else {
                videoCardView.visibility = View.GONE
            }
        })

        imagesViewModel.getImagesByMovieId(movieId)
            .observe(viewLifecycleOwner, { imagesItems ->
                val imageAdapter = ImagesAdapter(
                    imagesItems,
                )
                var tenImages = imagesItems.backdrops

                while (tenImages?.size!! > 10) {
                    tenImages = tenImages.dropLast(1) as MutableList<Backdrop>?
                }

                imagesItems.apply {
                    backdrops = tenImages
                }

                imagesRecyclerView.apply {
                    adapter = imageAdapter
                    layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                }
                imageAdapter.differ.submitList(imagesItems.backdrops)
            })

        buttonSeeAllImages.setOnClickListener {
            val action = MovieInfoFragmentDirections.actionMovieInfoFragmentToImagesFragment()
            action.movieId = movieId.toString()

            findNavController().navigate(action)
        }

        lists = myLists.getLists(accountId, sessionId, 1)

        menuButtonAddToList.setOnClickListener { v ->
            lists.observe(viewLifecycleOwner, {
                showAddToListMenu(v, R.menu.list_popup_menu, it.results!!)
            })
        }

        expandActivitiesButton.setOnClickListener {
            if (activitiesConstraintLayout.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(mainCardView)
                expandActivitiesButton.setImageResource(R.drawable.ic_round_expand_less_36)
                activitiesConstraintLayout.visibility = View.VISIBLE
            } else {
                expandActivitiesButton.setImageResource(R.drawable.ic_round_expand_more_36)
                activitiesConstraintLayout.visibility = View.GONE
            }
        }

        checkFavorites(buttonMarkMovieAsFavorite)
        checkWatchlist(buttonWatchlist)

        buttonMarkMovieAsFavorite.setOnClickListener {
            markAsFavorite(buttonMarkMovieAsFavorite)
        }

        buttonWatchlist.setOnClickListener {
            addToWatchlist(buttonWatchlist)
        }

        rateButton.setOnClickListener { TODO() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}