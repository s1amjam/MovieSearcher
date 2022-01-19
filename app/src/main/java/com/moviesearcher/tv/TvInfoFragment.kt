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
import com.moviesearcher.common.viewmodel.BaseViewModel
import com.moviesearcher.databinding.FragmentTvInfoBinding
import com.moviesearcher.list.lists.model.ListsResponse
import com.moviesearcher.movie.adapter.images.ImagesAdapter
import com.moviesearcher.movie.adapter.video.VideoAdapter
import com.moviesearcher.tv.adapter.cast.TvCastAdapter
import com.moviesearcher.tv.adapter.recommendations.TvRecommendationsAdapter
import com.moviesearcher.tv.model.cast.Cast
import com.moviesearcher.utils.Constants

private const val TAG = "TvInfoFragment"

class TvInfoFragment : BaseFragment() {
    private var _binding: FragmentTvInfoBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<TvInfoFragmentArgs>()

    private var numberOfSeasons: Int = 0

    private val viewModel: BaseViewModel by viewModels()

    private lateinit var tvInfoCastRecyclerView: RecyclerView
    private lateinit var recommendationsRecyclerView: RecyclerView
    private lateinit var videoRecyclerView: RecyclerView
    private lateinit var imagesRecyclerView: RecyclerView
    private lateinit var tvInfoPosterImageView: ImageView
    private lateinit var tvInfoTitle: TextView
    private lateinit var tvInfoRuntime: TextView
    private lateinit var tvInfoTagline: TextView
    private lateinit var releaseDate: TextView
    private lateinit var tvInfoOverview: TextView
    private lateinit var voteAverage: TextView
    private lateinit var voteCount: TextView
    private lateinit var tvInfoConstraintLayout: ConstraintLayout
    private lateinit var menuButtonAddToList: ImageButton
    private lateinit var buttonMarkTvAsFavorite: ImageButton
    private lateinit var buttonWatchlist: ImageButton
    private lateinit var buttonSeeAllImages: Button
    private lateinit var rateButton: Button
    private lateinit var director: TextView
    private lateinit var writer: TextView
    private lateinit var videoCardView: CardView
    private lateinit var trailerPreview: ImageView
    private lateinit var trailerName: TextView
    private lateinit var trailerCardView: CardView
    private lateinit var releaseDateDetail: TextView
    private lateinit var originCountry: TextView
    private lateinit var languageSpoken: TextView
    private lateinit var filmingLocations: TextView
    private lateinit var genresChipGroup: ChipGroup
    private lateinit var numberOfEpisodes: TextView
    private lateinit var expandActivitiesButton: ImageButton
    private lateinit var activitiesConstraintLayout: ConstraintLayout
    private lateinit var mainCardView: CardView
    private lateinit var progressBar: ProgressBar
    private lateinit var recommendationsCardView: CardView
    private lateinit var episodeGuideButton: Button

    private lateinit var lists: LiveData<ListsResponse>

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
        val tvId = args.tvId

        tvInfoCastRecyclerView = binding.castRecyclerView
        recommendationsRecyclerView = binding.recommendationsRecyclerView
        videoRecyclerView = binding.videoRecyclerView
        imagesRecyclerView = binding.imagesRecyclerView

        tvInfoConstraintLayout = binding.tvInfoConstraintLayout
        tvInfoPosterImageView = binding.tvInfoPosterImageView
        tvInfoTitle = binding.tvTitleTextView
        genresChipGroup = binding.chipGroupGenres
        tvInfoRuntime = binding.runtimeTextView
        tvInfoTagline = binding.taglineTextView
        releaseDate = binding.releaseDateTextView
        tvInfoOverview = binding.overviewTextView
        menuButtonAddToList = binding.menuButtonAddTvToList
        buttonMarkTvAsFavorite = binding.buttonMarkTvAsFavorite
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
        releaseDateDetail = binding.textviewReleaseDateDetail
        originCountry = binding.textviewOriginCountryDetail
        languageSpoken = binding.textviewLanguageSpokenDetail
        filmingLocations = binding.textviewFilmingLocationsDetail
        numberOfEpisodes = binding.numberOfEpisodesTextView
        expandActivitiesButton = binding.expandActivitiesButton
        activitiesConstraintLayout = binding.activitiesConstraintLayout
        mainCardView = binding.mainTvInfoCardView
        progressBar = binding.progressBarTvInfo
        recommendationsCardView = binding.recommendationsCardView
        episodeGuideButton = binding.episodeGuideButton

        tvInfoConstraintLayout.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE

        menuButtonAddToList.isVisible = sessionId != ""
        buttonMarkTvAsFavorite.isVisible = sessionId != ""
        buttonWatchlist.isVisible = sessionId != ""

        viewModel.getTvInfoById(tvId).observe(
            viewLifecycleOwner,
            { tvInfo ->
                val minutes = tvInfo.episodeRunTime?.get(0)?.toLong()
                val languages = mutableListOf<String>()
                val locations = mutableListOf<String>()
                val genres = tvInfo.genres
                val lastAirDate: String

                numberOfSeasons = tvInfo.numberOfSeasons!!

                tvInfo.spokenLanguages?.forEach { languages.add(it.name!!) }
                tvInfo.productionCountries?.forEach { locations.add(it.name!!) }

                Glide.with(this)
                    .load(Constants.IMAGE_URL + tvInfo.posterPath)
                    .placeholder(R.drawable.ic_placeholder)
                    .centerCrop()
                    .override(300, 500)
                    .into(tvInfoPosterImageView)
                tvInfoTitle.text = tvInfo.name
                tvInfoRuntime.text = String.format("%02dm", minutes).dropWhile { it == '0' }
                tvInfoTagline.text = tvInfo.tagline
                val firstAirDate: String = tvInfo.firstAirDate?.dropLast(6).toString()
                if (tvInfo.inProduction == false && tvInfo.lastAirDate != null) {
                    lastAirDate = tvInfo.lastAirDate.dropLast(6)
                    releaseDate.text = getString(R.string.tv_series_finished_date_range).format(
                        firstAirDate,
                        lastAirDate
                    )
                } else {
                    releaseDate.text =
                        getString(R.string.tv_series_in_production_date_range).format(firstAirDate)
                }
                tvInfoOverview.text = tvInfo.overview
                voteAverage.text = getString(R.string.vote).format(tvInfo.getAverage())
                voteCount.text = tvInfo.voteCount.toString()
                releaseDateDetail.text = tvInfo.firstAirDate?.replace("-", ".")
                originCountry.text = tvInfo.productionCountries?.get(0)?.name
                languageSpoken.text = languages.joinToString()
                filmingLocations.text = locations.joinToString()
                numberOfEpisodes.text =
                    getString(R.string.count_of_episodes).format(tvInfo.numberOfEpisodes.toString())

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

                tvInfoOverview.setOnClickListener {
                    MaterialAlertDialogBuilder(requireContext()).setMessage(tvInfo.overview)
                        .show()
                }

                tvInfoConstraintLayout.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            })

        checkWatchlist(buttonWatchlist)
        checkFavorites(buttonMarkTvAsFavorite)

        viewModel.getTvCastById(tvId).observe(viewLifecycleOwner, { castItems ->
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

            tvInfoCastRecyclerView.apply {
                adapter = tvCastAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
            tvCastAdapter.differ.submitList(castItems.cast)

            castItems.crew?.filter { it.known_for_department == "Directing" }
                .also { it -> it?.forEach { directors.add(it.original_name!!) } }
            castItems.crew?.filter { it.known_for_department == "Writing" }
                .also { it -> it?.forEach { writers.add(it.original_name!!) } }

            director.text = getString(R.string.director).format(directors.joinToString())
            writer.text = getString(R.string.writer).format(writers.joinToString())
        })

        viewModel.getRecommendationsByTvId(tvId)
            .observe(viewLifecycleOwner, { recommendationsItems ->
                if (recommendationsItems.totalResults == 0) {
                    recommendationsCardView.visibility = View.GONE
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
                }
            })

        viewModel.getVideosByTvId(tvId).observe(viewLifecycleOwner, { videoItems ->
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
                            TvInfoFragmentDirections.actionTvInfoFragmentToVideoFragment(
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

        viewModel.getImagesByTvId(tvId)
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
            val action = TvInfoFragmentDirections.actionTvInfoFragmentToImagesFragment()
            action.tvId = tvId.toString()

            findNavController().navigate(action)
        }

        lists = viewModel.getLists(accountId, sessionId, 1)

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

        episodeGuideButton.setOnClickListener {
            findNavController().navigate(
                TvInfoFragmentDirections.actionTvInfoFragmentToTvSeasonsFragment(
                    tvId,
                    numberOfSeasons
                )
            )
        }

        buttonMarkTvAsFavorite.setOnClickListener {
            markAsFavorite(buttonMarkTvAsFavorite)
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