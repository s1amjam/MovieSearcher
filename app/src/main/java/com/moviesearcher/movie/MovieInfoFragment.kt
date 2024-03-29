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
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moviesearcher.R
import com.moviesearcher.common.PosterDialog
import com.moviesearcher.common.RateDialog
import com.moviesearcher.common.credentials.CredentialsHolder
import com.moviesearcher.common.extensions.loadImage
import com.moviesearcher.common.extensions.toOneScale
import com.moviesearcher.common.model.images.Backdrop
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.common.utils.Constants.ERROR_MESSAGE
import com.moviesearcher.common.utils.Constants.LIST_ID
import com.moviesearcher.common.utils.Status
import com.moviesearcher.databinding.FragmentMovieInfoBinding
import com.moviesearcher.favorite.FavoriteViewModel
import com.moviesearcher.favorite.model.MarkAsFavoriteRequest
import com.moviesearcher.list.ListViewModel
import com.moviesearcher.list.lists.ListsViewModel
import com.moviesearcher.movie.adapter.cast.MovieCastAdapter
import com.moviesearcher.movie.adapter.images.ImagesAdapter
import com.moviesearcher.movie.adapter.recommendations.RecommendationsAdapter
import com.moviesearcher.movie.adapter.video.VideoAdapter
import com.moviesearcher.movie.model.cast.Cast
import com.moviesearcher.watchlist.common.model.WatchlistRequest
import com.moviesearcher.watchlist.common.viewmodel.WatchlistViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.set

@AndroidEntryPoint
class MovieInfoFragment : Fragment() {
    private var _binding: FragmentMovieInfoBinding? = null
    private val binding get() = _binding!!

    private val mediaInfo: MutableMap<String, Long> = mutableMapOf()

    private val args by navArgs<MovieInfoFragmentArgs>()

    @Inject
    lateinit var credentialsHolder: CredentialsHolder
    private val sessionId: String
        get() = credentialsHolder.getSessionId()

    private val movieViewModel by viewModels<MovieViewModel>()
    private val listsViewModel by viewModels<ListsViewModel>()
    private val watchlistViewModel by viewModels<WatchlistViewModel>()
    private val listViewModel by viewModels<ListViewModel>()
    private val favoriteViewModel by viewModels<FavoriteViewModel>()

    private lateinit var castRecyclerView: RecyclerView
    private lateinit var recommendationsRecyclerView: RecyclerView
    private lateinit var videoRecyclerView: RecyclerView
    private lateinit var imagesRecyclerView: RecyclerView

    private lateinit var posterImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var runtimeTextView: TextView
    private lateinit var taglineTextView: TextView
    private lateinit var releaseDateTextView: TextView
    private lateinit var overviewTextView: TextView
    private lateinit var voteAverageTextView: TextView
    private lateinit var voteCountTextView: TextView
    private lateinit var mainConstraintLayout: ConstraintLayout
    private lateinit var addToListImageButton: ImageButton
    private lateinit var markMovieAsFavoriteImageButton: ImageButton
    private lateinit var watchlistImageButton: ImageButton
    private lateinit var seeAllImagesButton: Button
    private lateinit var rateButton: Button
    private lateinit var directorTextView: TextView
    private lateinit var writerTextView: TextView
    private lateinit var videoCardView: CardView
    private lateinit var castCardView: CardView
    private lateinit var imagesCardView: CardView
    private lateinit var trailerPreviewImageView: ImageView
    private lateinit var trailerNameTextView: TextView
    private lateinit var trailerCardView: CardView
    private lateinit var releaseDateDetailTextView: TextView
    private lateinit var originCountryTextView: TextView
    private lateinit var languageSpokenTextView: TextView
    private lateinit var filmingLocationsTextView: TextView
    private lateinit var genresChipGroup: ChipGroup
    private lateinit var expandActivitiesImageButton: ImageButton
    private lateinit var activitiesConstraintLayout: ConstraintLayout
    private lateinit var mainCardView: CardView
    private lateinit var progressBar: ProgressBar
    private lateinit var recommendationsCardView: CardView
    private lateinit var moreLikeThisTitle: TextView
    private lateinit var imagesAdapter: ImagesAdapter

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
        castRecyclerView = binding.castRecyclerView
        recommendationsRecyclerView = binding.moreLikeThisCardview.recyclerView
        videoRecyclerView = binding.videoRecyclerView
        imagesRecyclerView = binding.imagesRecyclerView

        mainConstraintLayout = binding.movieInfoConstraintLayout
        posterImageView = binding.movieInfoPosterImageView
        titleTextView = binding.movieTitleTextView
        genresChipGroup = binding.chipGroupGenres
        runtimeTextView = binding.runtimeTextView
        taglineTextView = binding.taglineTextView
        releaseDateTextView = binding.releaseDateTextView
        overviewTextView = binding.overviewTextView
        addToListImageButton = binding.menuButtonAddMovieToList
        markMovieAsFavoriteImageButton = binding.buttonMarkMovieAsFavorite
        watchlistImageButton = binding.buttonWatchlist
        seeAllImagesButton = binding.buttonSeeAllImages
        voteAverageTextView = binding.textViewRating
        voteCountTextView = binding.textViewVoteCount
        rateButton = binding.rateButtonView
        directorTextView = binding.directorCastTextView
        writerTextView = binding.writerCastTextView
        videoCardView = binding.videoCardView
        castCardView = binding.castCv
        imagesCardView = binding.imagesCardView
        trailerPreviewImageView = binding.previewTrailerImageView
        trailerNameTextView = binding.trailerNameTextView
        trailerCardView = binding.trailerCardView
        releaseDateDetailTextView = binding.textviewReleaseDateDetail
        originCountryTextView = binding.textviewOriginCountryDetail
        languageSpokenTextView = binding.textviewLanguageSpokenDetail
        filmingLocationsTextView = binding.textviewFilmingLocationsDetail
        expandActivitiesImageButton = binding.expandActivitiesButton
        activitiesConstraintLayout = binding.activitiesConstraintLayout
        mainCardView = binding.mainMovieInfoCardView
        progressBar = binding.progressBarMovieInfo
        recommendationsCardView = binding.moreLikeThisCardview.cardView
        moreLikeThisTitle = binding.moreLikeThisCardview.titleTextview

        moreLikeThisTitle.text = getString(R.string.more_like_this)

        addToListImageButton.isVisible = sessionId != ""
        markMovieAsFavoriteImageButton.isVisible = sessionId != ""
        watchlistImageButton.isVisible = sessionId != ""

        mediaInfo["movie"] = args.movieId

        setupImagesAdapter()
        setupObservers(savedInstanceState)
    }

    private fun setupObservers(savedInstanceState: Bundle?) {
        movieViewModel.getMovieInfo().observe(viewLifecycleOwner) { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { movieInfo ->
                        val posterDialog = PosterDialog(movieInfo.posterPath.toString())
                        val rateDialog =
                            RateDialog(args.movieId, sessionId, movieViewModel = movieViewModel)
                        val hours = TimeUnit.MINUTES.toHours(movieInfo.runtime?.toLong()!!)
                        val minutes = movieInfo.runtime.toLong() - TimeUnit.HOURS.toMinutes(hours)
                        val languages = mutableListOf<String>()
                        val locations = mutableListOf<String>()
                        val genres = movieInfo.genres

                        movieInfo.spokenLanguages?.forEach { languages.add(it.name!!) }
                        movieInfo.productionCountries?.forEach { locations.add(it.name!!) }

                        posterImageView.loadImage(Constants.IMAGE_URL + movieInfo.posterPath)
                        titleTextView.text = movieInfo.title
                        runtimeTextView.text =
                            String.format("%02dh %02dm", hours, minutes).dropWhile { it == '0' }
                        taglineTextView.text = movieInfo.tagline
                        releaseDateTextView.text = movieInfo.releaseDate?.dropLast(6)
                        overviewTextView.text = movieInfo.overview
                        voteAverageTextView.text =
                            getString(R.string.vote).format(movieInfo.voteAverage?.toOneScale())
                        voteCountTextView.text = movieInfo.voteCount.toString()
                        releaseDateDetailTextView.text = movieInfo.releaseDate?.replace("-", ".")
                        originCountryTextView.text =
                            movieInfo.productionCountries?.elementAtOrNull(0)?.name
                        languageSpokenTextView.text = languages.joinToString()
                        filmingLocationsTextView.text = locations.joinToString()

                        if (taglineTextView.text == "") {
                            taglineTextView.visibility = View.GONE
                        }

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

                        overviewTextView.setOnClickListener {
                            MaterialAlertDialogBuilder(requireContext())
                                .setMessage(movieInfo.overview)
                                .show()
                        }

                        posterImageView.setOnClickListener {
                            posterDialog.show(childFragmentManager, "PosterDialogFragment")
                        }

                        if (sessionId.isNotEmpty()) {
                            rateButton.setOnClickListener {
                                rateDialog.show(childFragmentManager, "RateDialogFragment")
                            }

                            movieViewModel.getAccountStates().observe(viewLifecycleOwner) {
                                when (it.status) {
                                    Status.SUCCESS -> {
                                        it.data?.let { accountState ->
                                            if (accountState.rated.toString() != "false") {
                                                rateButton.text =
                                                    getString(R.string.your_rating).format(
                                                        accountState.rated?.value.toString()
                                                    )
                                            }
                                        }
                                    }
                                    Status.LOADING -> {

                                    }
                                    Status.ERROR -> {

                                    }
                                }
                            }
                        } else {
                            rateButton.setOnClickListener {
                                Toast.makeText(
                                    requireContext(),
                                    "Please log in to rate the movie",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        mainConstraintLayout.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    mainConstraintLayout.visibility = View.GONE
                }
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        ERROR_MESSAGE.format(it.message),
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }
        }

        movieViewModel.getMovieCast().observe(viewLifecycleOwner) { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { castItems ->
                        if (!castItems.cast.isNullOrEmpty()) {
                            val movieCastAdapter = MovieCastAdapter(findNavController())
                            var tenCast = castItems.cast

                            while (tenCast?.size!! > 10) {
                                tenCast = tenCast.dropLast(1) as MutableList<Cast>?
                            }

                            castItems.apply {
                                cast = tenCast
                            }

                            castRecyclerView.apply {
                                adapter = movieCastAdapter
                                layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                            }
                            movieCastAdapter.submitList(castItems.cast)

                            val director = castItems.crew?.find { it.job == "Director" }?.name
                            if (director?.isNotEmpty() == true) {
                                directorTextView.text =
                                    getString(R.string.director).format(
                                        castItems.crew.find { it.job == "Director" }?.name
                                    )
                            } else {
                                directorTextView.visibility = View.GONE
                            }

                            val writer = castItems.crew?.find { it.job == "Writing" }?.name
                            if (writer?.isNotEmpty() == true) {
                                writerTextView.text =
                                    getString(R.string.writer).format(
                                        castItems.crew.find { it.job == "Writing" }?.name
                                    )
                            } else {
                                writerTextView.visibility = View.GONE
                            }

                            progressBar.visibility = View.GONE
                            castCardView.visibility = View.VISIBLE
                        } else {
                            progressBar.visibility = View.GONE
                        }
                    }
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    castCardView.visibility = View.GONE
                }
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        ERROR_MESSAGE.format(it.message),
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }
        }

        movieViewModel.getRecommendations().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { recommendationsItems ->
                        if (recommendationsItems.totalResults == 0) {
                            progressBar.visibility = View.GONE
                        } else {
                            val recommendationsAdapter = RecommendationsAdapter(
                                recommendationsItems,
                                findNavController()
                            )

                            recommendationsRecyclerView.apply {
                                adapter = recommendationsAdapter
                                layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                            }
                            recommendationsAdapter.differ.submitList(recommendationsItems.results)

                            progressBar.visibility = View.GONE
                            recommendationsCardView.visibility = View.VISIBLE
                        }
                    }
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recommendationsCardView.visibility = View.GONE
                }
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        ERROR_MESSAGE.format(it.message),
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }
        }

        movieViewModel.getVideos().observe(viewLifecycleOwner) { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { videoItems ->
                        if (!videoItems.results.isNullOrEmpty()) {
                            val videoAdapter = VideoAdapter(
                                videoItems,
                                findNavController()
                            )

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

                                trailerPreviewImageView.loadImage(
                                    Constants.YOUTUBE_PREVIEW_URL.format(
                                        officialTrailer.key
                                    ), isYouTube = true
                                )

                                trailerNameTextView.text = officialTrailer.name

                                trailerCardView.setOnClickListener {
                                    findNavController().navigate(
                                        MovieInfoFragmentDirections
                                            .actionMovieInfoFragmentToVideoFragment(
                                                officialTrailer.key!!
                                            )
                                    )
                                }
                            } else {
                                trailerCardView.visibility = View.GONE
                            }

                            videoRecyclerView.apply {
                                adapter = videoAdapter
                                layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                            }
                            videoAdapter.differ.submitList(videoItems.results)

                            progressBar.visibility = View.GONE
                            videoCardView.visibility = View.VISIBLE
                        } else {
                            progressBar.visibility = View.GONE
                        }
                    }
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    videoCardView.visibility = View.GONE
                }
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        ERROR_MESSAGE.format(it.message),
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }
        }

        movieViewModel.getImages().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { imagesItems ->
                        if (!imagesItems.backdrops.isNullOrEmpty()) {
                            var tenImages = imagesItems.backdrops

                            while (tenImages?.size!! > 10) {
                                tenImages = tenImages.dropLast(1) as MutableList<Backdrop>?
                            }

                            imagesItems.apply {
                                backdrops = tenImages
                            }

                            imagesAdapter.submitList(imagesItems.backdrops)

                            imagesCardView.visibility = View.VISIBLE
                        }

                        progressBar.visibility = View.GONE
                    }
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    imagesCardView.visibility = View.GONE
                }
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        ERROR_MESSAGE.format(it.message),
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }
        }

        seeAllImagesButton.setOnClickListener {
            val action = MovieInfoFragmentDirections.actionMovieInfoFragmentToImagesFragment()
            action.movieId = args.movieId

            findNavController().navigate(action)
        }

        if (sessionId.isNotEmpty()) {
            addToListImageButton.isClickable = true

            listsViewModel.getLists().observe(viewLifecycleOwner) { lists ->
                lists.data?.results?.get(0)?.id?.toInt()
                    ?.let { it1 -> savedInstanceState?.putInt(LIST_ID, it1) }

                when (lists.status) {
                    Status.SUCCESS -> {
                        lists.data?.let {
                            addToListImageButton.setOnClickListener { v ->
                                listViewModel.showAddToListMenu(
                                    v,
                                    R.menu.list_popup_menu,
                                    it.results,
                                    mediaInfo,
                                    viewLifecycleOwner,
                                    requireContext(),
                                    sessionId,
                                    childFragmentManager
                                )
                            }
                        }
                    }
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE

                        Toast.makeText(
                            requireContext(),
                            ERROR_MESSAGE.format(lists.message),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            expandActivitiesImageButton.setOnClickListener {
                if (activitiesConstraintLayout.visibility == View.GONE) {
                    TransitionManager.beginDelayedTransition(mainCardView)
                    expandActivitiesImageButton.setImageResource(R.drawable.ic_round_expand_less_36)
                    activitiesConstraintLayout.visibility = View.VISIBLE
                } else {
                    expandActivitiesImageButton.setImageResource(R.drawable.ic_round_expand_more_36)
                    activitiesConstraintLayout.visibility = View.GONE
                }
            }

            favoriteViewModel.checkFavorites(
                markMovieAsFavoriteImageButton,
                viewLifecycleOwner,
                mediaInfo,
                requireContext()
            )

            watchlistViewModel.checkWatchlist(
                watchlistImageButton,
                mediaInfo,
                viewLifecycleOwner,
                requireContext()
            )

            markMovieAsFavoriteImageButton.setOnClickListener {
                favoriteViewModel.postMarkAsFavorite(
                    MarkAsFavoriteRequest(
                        markMovieAsFavoriteImageButton.tag.toString().toBoolean(),
                        mediaInfo.values.first(),
                        mediaInfo.keys.first()
                    )
                )

                favoriteViewModel.processFavoriteButtons(markMovieAsFavoriteImageButton)
            }

            watchlistImageButton.setOnClickListener {
                watchlistViewModel.postWatchlist(
                    WatchlistRequest(
                        watchlistImageButton.tag.toString().toBoolean(),
                        mediaInfo.values.first(),
                        mediaInfo.keys.first()
                    )
                )

                watchlistViewModel.processWatchlistButtons(watchlistImageButton)
            }
        } else {
            addToListImageButton.isClickable = false

            expandActivitiesImageButton.setOnClickListener {
                Toast.makeText(
                    requireContext(),
                    "Please log in to have access to personal functions",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupImagesAdapter() {
        imagesAdapter = ImagesAdapter() {
            PosterDialog(it).show(childFragmentManager, "PosterDialog")
        }

        imagesRecyclerView.adapter = imagesAdapter
        imagesRecyclerView.layoutManager =
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}