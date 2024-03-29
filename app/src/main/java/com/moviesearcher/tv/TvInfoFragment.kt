package com.moviesearcher.tv

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
import com.moviesearcher.databinding.FragmentTvInfoBinding
import com.moviesearcher.favorite.FavoriteViewModel
import com.moviesearcher.favorite.model.MarkAsFavoriteRequest
import com.moviesearcher.list.ListViewModel
import com.moviesearcher.list.lists.ListsViewModel
import com.moviesearcher.movie.adapter.images.ImagesAdapter
import com.moviesearcher.movie.adapter.video.VideoAdapter
import com.moviesearcher.tv.adapter.cast.TvCastAdapter
import com.moviesearcher.tv.adapter.recommendations.TvRecommendationsAdapter
import com.moviesearcher.tv.model.cast.Cast
import com.moviesearcher.watchlist.common.model.WatchlistRequest
import com.moviesearcher.watchlist.common.viewmodel.WatchlistViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TvInfoFragment : Fragment() {

    private var _binding: FragmentTvInfoBinding? = null
    private val binding get() = _binding!!

    private val mediaInfo: MutableMap<String, Long> = mutableMapOf()

    private val args by navArgs<TvInfoFragmentArgs>()
    private var numberOfSeasons: Int = 0

    @Inject
    lateinit var credentialsHolder: CredentialsHolder
    private val sessionId: String
        get() = credentialsHolder.getSessionId()

    private val tvViewModel by viewModels<TvViewModel>()
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
    private lateinit var markTvAsFavoriteButton: ImageButton
    private lateinit var watchlistImageButton: ImageButton
    private lateinit var seeAllImagesButton: Button
    private lateinit var rateButton: Button
    private lateinit var directorTextView: TextView
    private lateinit var writerTextView: TextView
    private lateinit var videoCardView: CardView
    private lateinit var trailerPreviewImageView: ImageView
    private lateinit var trailerNameTextView: TextView
    private lateinit var trailerCardView: CardView
    private lateinit var releaseDateDetailTextView: TextView
    private lateinit var originCountryTextView: TextView
    private lateinit var languageSpokenTextView: TextView
    private lateinit var filmingLocationsTextView: TextView
    private lateinit var genresChipGroup: ChipGroup
    private lateinit var numberOfEpisodesTextView: TextView
    private lateinit var expandActivitiesButton: ImageButton
    private lateinit var activitiesConstraintLayout: ConstraintLayout
    private lateinit var mainCardView: CardView
    private lateinit var progressBar: ProgressBar
    private lateinit var recommendationsCardView: CardView
    private lateinit var episodeGuideButton: Button
    private lateinit var castCardView: CardView
    private lateinit var imagesCardView: CardView
    private lateinit var moreLikeThisTitle: TextView
    private lateinit var imagesAdapter: ImagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        castRecyclerView = binding.castRecyclerView
        recommendationsRecyclerView = binding.moreLikeThisCardview.recyclerView
        videoRecyclerView = binding.videoRecyclerView
        imagesRecyclerView = binding.imagesRecyclerView

        mainConstraintLayout = binding.tvInfoConstraintLayout
        posterImageView = binding.tvInfoPosterImageView
        titleTextView = binding.tvTitleTextView
        genresChipGroup = binding.chipGroupGenres
        runtimeTextView = binding.runtimeTextView
        taglineTextView = binding.taglineTextView
        releaseDateTextView = binding.releaseDateTextView
        overviewTextView = binding.overviewTextView
        addToListImageButton = binding.menuButtonAddTvToList
        markTvAsFavoriteButton = binding.buttonMarkTvAsFavorite
        watchlistImageButton = binding.buttonWatchlist
        seeAllImagesButton = binding.buttonSeeAllImages
        voteAverageTextView = binding.textViewRating
        voteCountTextView = binding.textViewVoteCount
        rateButton = binding.rateButtonView
        directorTextView = binding.directorCastTextView
        writerTextView = binding.writerCastTextView
        videoCardView = binding.videoCardView
        trailerPreviewImageView = binding.previewTrailerImageView
        trailerNameTextView = binding.trailerNameTextView
        trailerCardView = binding.trailerCardView
        releaseDateDetailTextView = binding.textviewReleaseDateDetail
        originCountryTextView = binding.textviewOriginCountryDetail
        languageSpokenTextView = binding.textviewLanguageSpokenDetail
        filmingLocationsTextView = binding.textviewFilmingLocationsDetail
        numberOfEpisodesTextView = binding.numberOfEpisodesTextView
        expandActivitiesButton = binding.expandActivitiesButton
        activitiesConstraintLayout = binding.activitiesConstraintLayout
        mainCardView = binding.mainTvInfoCardView
        progressBar = binding.progressBarTvInfo
        recommendationsCardView = binding.moreLikeThisCardview.cardView
        episodeGuideButton = binding.episodeGuideButton
        castCardView = binding.castCv
        imagesCardView = binding.imagesCardView
        moreLikeThisTitle = binding.moreLikeThisCardview.titleTextview

        moreLikeThisTitle.text = getString(R.string.more_like_this)

        addToListImageButton.isVisible = sessionId != ""
        markTvAsFavoriteButton.isVisible = sessionId != ""
        watchlistImageButton.isVisible = sessionId != ""

        mediaInfo["tv"] = args.tvId

        setupImagesAdapter()
        setupUi(savedInstanceState)
    }

    private fun setupUi(savedInstanceState: Bundle?) {
        tvViewModel.getTvInfo().observe(viewLifecycleOwner) { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { tvInfo ->
                        val dialog = PosterDialog(tvInfo.posterPath.toString())
                        val rateDialog = RateDialog(args.tvId, sessionId, tvViewModel = tvViewModel)
                        var minutes: Long = 0
                        val languages = mutableListOf<String>()
                        val locations = mutableListOf<String>()
                        val genres = tvInfo.genres
                        val lastAirDate: String

                        if (tvInfo.episodeRunTime?.isNotEmpty() == true) {
                            minutes = tvInfo.episodeRunTime[0].toLong()
                        }

                        tvInfo.numberOfSeasons.let {
                            if (it != null) {
                                numberOfSeasons = it
                            }
                        }

                        tvInfo.spokenLanguages?.forEach { languages.add(it.name!!) }
                        tvInfo.productionCountries?.forEach { locations.add(it.name!!) }

                        tvInfo.posterPath?.let { it1 ->
                            posterImageView.loadImage(Constants.IMAGE_URL + it1)
                        }
                        titleTextView.text = tvInfo.name
                        runtimeTextView.text =
                            String.format("%02dm", minutes).dropWhile { it == '0' }
                        taglineTextView.text = tvInfo.tagline
                        val firstAirDate: String = tvInfo.firstAirDate?.dropLast(6).toString()
                        if (tvInfo.inProduction == false && tvInfo.lastAirDate != null) {
                            lastAirDate = tvInfo.lastAirDate.dropLast(6)
                            releaseDateTextView.text =
                                getString(R.string.tv_series_finished_date_range).format(
                                    firstAirDate,
                                    lastAirDate
                                )
                        } else {
                            releaseDateTextView.text =
                                getString(R.string.tv_series_in_production_date_range).format(
                                    firstAirDate
                                )
                        }
                        overviewTextView.text = tvInfo.overview
                        voteAverageTextView.text =
                            getString(R.string.vote).format(tvInfo.voteAverage?.toOneScale())
                        voteCountTextView.text = tvInfo.voteCount.toString()
                        releaseDateDetailTextView.text = tvInfo.firstAirDate?.replace("-", ".")
                        originCountryTextView.text =
                            tvInfo.productionCountries?.elementAtOrNull(0)?.name
                        languageSpokenTextView.text = languages.joinToString()
                        filmingLocationsTextView.text = locations.joinToString()
                        numberOfEpisodesTextView.text =
                            getString(R.string.count_of_episodes).format(tvInfo.numberOfEpisodes.toString())

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
                            MaterialAlertDialogBuilder(requireContext()).setMessage(tvInfo.overview)
                                .show()
                        }

                        posterImageView.setOnClickListener {
                            dialog.show(childFragmentManager, "PosterDialogFragment")
                        }

                        if (sessionId.isNotEmpty()) {
                            rateButton.setOnClickListener {
                                rateDialog.show(childFragmentManager, "RateDialogFragment")
                            }

                            tvViewModel.getAccountStates().observe(viewLifecycleOwner) {
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

                        progressBar.visibility = View.GONE
                        mainConstraintLayout.visibility = View.VISIBLE
                    }
                }
                Status.LOADING -> {
                    mainConstraintLayout.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
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

            tvViewModel.getTvCast().observe(viewLifecycleOwner) { it ->
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { castItems ->
                            if (!castItems.cast.isNullOrEmpty()) {
                                val tvCastAdapter = TvCastAdapter(castItems, findNavController())
                                val directors = mutableListOf<String>()
                                val writers = mutableListOf<String>()
                                var tenCast = castItems.cast

                                while (tenCast?.size!! > 10) {
                                    tenCast = tenCast.dropLast(1) as MutableList<Cast>?
                                }

                                castItems.apply {
                                    cast = tenCast
                                }

                                castRecyclerView.apply {
                                    adapter = tvCastAdapter
                                    layoutManager =
                                        LinearLayoutManager(
                                            requireContext(),
                                            LinearLayoutManager.HORIZONTAL,
                                            false
                                        )
                                }
                                tvCastAdapter.differ.submitList(castItems.cast)

                                val director =
                                    castItems.crew?.filter {
                                        it.known_for_department == "Directing"
                                    }
                                        .also { it ->
                                            it?.forEach {
                                                directors.add(it.original_name!!)
                                            }
                                        }

                                if (director?.isNotEmpty() == true) {
                                    directorTextView.text =
                                        getString(R.string.director).format(directors.joinToString())
                                } else {
                                    directorTextView.visibility = View.GONE
                                }


                                val writer =
                                    castItems.crew?.filter {
                                        it.known_for_department == "Writing"
                                    }
                                        .also { it ->
                                            it?.forEach {
                                                writers.add(it.original_name!!)
                                            }
                                        }

                                if (writer?.isNotEmpty() == true) {
                                    writerTextView.text =
                                        getString(R.string.writer).format(writers.joinToString())
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
                        castCardView.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
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

            tvViewModel.getRecommendations().observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { recommendationsItems ->
                            if (recommendationsItems.totalResults == 0) {
                                recommendationsCardView.visibility = View.GONE
                                progressBar.visibility = View.GONE
                            } else {
                                val tvRecommendationsAdapter = TvRecommendationsAdapter(
                                    recommendationsItems,
                                    findNavController()
                                )

                                recommendationsRecyclerView.apply {
                                    adapter = tvRecommendationsAdapter
                                    layoutManager =
                                        LinearLayoutManager(
                                            requireContext(),
                                            LinearLayoutManager.HORIZONTAL,
                                            false
                                        )
                                }
                                tvRecommendationsAdapter.differ.submitList(recommendationsItems.results)

                                progressBar.visibility = View.GONE
                                recommendationsCardView.visibility = View.VISIBLE
                            }
                        }
                    }
                    Status.LOADING -> {
                        recommendationsCardView.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
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

            tvViewModel.getVideos().observe(viewLifecycleOwner) { it ->
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
                                        ), true
                                    )

                                    trailerNameTextView.text = officialTrailer.name

                                    trailerCardView.setOnClickListener {
                                        findNavController().navigate(
                                            TvInfoFragmentDirections.actionTvInfoFragmentToVideoFragment(
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
                                videoCardView.visibility = View.GONE
                            }
                        }
                    }
                    Status.LOADING -> {
                        videoCardView.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
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

            tvViewModel.getImages().observe(viewLifecycleOwner) {
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

                                progressBar.visibility = View.GONE
                                imagesCardView.visibility = View.VISIBLE
                            } else {
                                progressBar.visibility = View.GONE
                            }
                        }
                    }
                    Status.LOADING -> {
                        imagesCardView.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
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
                val action = TvInfoFragmentDirections.actionTvInfoFragmentToImagesFragment()
                action.tvId = args.tvId

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
                            Toast.makeText(
                                requireContext(),
                                ERROR_MESSAGE.format(lists.message),
                                Toast.LENGTH_LONG
                            ).show()
                            progressBar.visibility = View.GONE
                        }
                    }
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

                watchlistViewModel.checkWatchlist(
                    watchlistImageButton,
                    mediaInfo,
                    viewLifecycleOwner,
                    requireContext(),
                    true
                )

                favoriteViewModel.checkFavorites(
                    markTvAsFavoriteButton,
                    viewLifecycleOwner,
                    mediaInfo,
                    requireContext()
                )

                markTvAsFavoriteButton.setOnClickListener {
                    favoriteViewModel.postMarkAsFavorite(
                        MarkAsFavoriteRequest(
                            markTvAsFavoriteButton.tag.toString().toBoolean(),
                            mediaInfo.values.first(),
                            mediaInfo.keys.first()
                        )
                    )

                    favoriteViewModel.processFavoriteButtons(markTvAsFavoriteButton)
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

                expandActivitiesButton.setOnClickListener {
                    Toast.makeText(
                        requireContext(),
                        "Please log in to have access to personal functions",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        episodeGuideButton.setOnClickListener {
            findNavController().navigate(
                TvInfoFragmentDirections.actionTvInfoFragmentToTvSeasonsFragment(
                    args.tvId,
                    numberOfSeasons
                )
            )
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