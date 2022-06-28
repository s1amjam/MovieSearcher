package com.moviesearcher.tv.episode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moviesearcher.R
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.common.PosterDialog
import com.moviesearcher.common.toOneScale
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.common.utils.Status
import com.moviesearcher.common.viewmodel.ViewModelFactory
import com.moviesearcher.databinding.FragmentTvEpisodeBinding
import com.moviesearcher.movie.adapter.video.VideoAdapter
import com.moviesearcher.tv.episode.adapter.cast.TvEpisodeCastAdapter
import com.moviesearcher.tv.episode.adapter.images.EpisodeImagesAdapter
import com.moviesearcher.tv.episode.model.Crew
import com.moviesearcher.tv.episode.model.image.Still

private const val TAG = "TvEpisodeFragment"

class TvEpisodeFragment : BaseFragment() {
    private var _binding: FragmentTvEpisodeBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<TvEpisodeFragmentArgs>()

    private lateinit var viewModel: TvEpisodeViewModel

    private lateinit var tvInfoCastRecyclerView: RecyclerView
    private lateinit var videoRecyclerView: RecyclerView
    private lateinit var imagesRecyclerView: RecyclerView
    private lateinit var tvInfoPosterImageView: ImageView
    private lateinit var tvInfoTitle: TextView
    private lateinit var tvInfoRuntime: TextView
    private lateinit var releaseDate: TextView
    private lateinit var tvInfoOverview: TextView
    private lateinit var voteAverage: TextView
    private lateinit var voteCount: TextView
    private lateinit var tvInfoConstraintLayout: ConstraintLayout
    private lateinit var buttonSeeAllImages: Button
    private lateinit var director: TextView
    private lateinit var writer: TextView
    private lateinit var videoCardView: CardView
    private lateinit var trailerPreview: ImageView
    private lateinit var trailerName: TextView
    private lateinit var trailerCardView: CardView
    private lateinit var genresChipGroup: ChipGroup
    private lateinit var numberOfEpisodes: TextView
    private lateinit var mainCardView: CardView
    private lateinit var progressBar: ProgressBar
    private lateinit var episodeGuideButton: Button
    private lateinit var imagesCardView: CardView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvEpisodeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvId = args.tvId

        tvInfoCastRecyclerView = binding.castRecyclerView
        videoRecyclerView = binding.videoRecyclerView
        imagesRecyclerView = binding.imagesRecyclerView

        tvInfoConstraintLayout = binding.tvInfoConstraintLayout
        tvInfoPosterImageView = binding.tvInfoPosterImageView
        tvInfoTitle = binding.tvTitleTextView
        genresChipGroup = binding.chipGroupGenres
        tvInfoRuntime = binding.runtimeTextView
        releaseDate = binding.releaseDateTextView
        tvInfoOverview = binding.overviewTextView
        buttonSeeAllImages = binding.buttonSeeAllImages
        voteAverage = binding.textViewRating
        voteCount = binding.textViewVoteCount
        director = binding.directorCastTextView
        writer = binding.writerCastTextView
        videoCardView = binding.videoCardView
        trailerPreview = binding.previewTrailerImageView
        trailerName = binding.trailerNameTextView
        trailerCardView = binding.trailerCardView
        numberOfEpisodes = binding.numberOfEpisodesTextView
        mainCardView = binding.mainTvInfoCardView
        progressBar = binding.progressBarTvInfo
        episodeGuideButton = binding.episodeGuideButton
        imagesCardView = binding.imagesCardView

        setupViewModel()

        viewModel.getTvEpisode()
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { tvInfo ->
                            val dialog = PosterDialog(tvInfo.stillPath.toString())
                            Glide.with(this)
                                .load(Constants.IMAGE_URL + tvInfo.stillPath)
                                .placeholder(R.drawable.ic_placeholder)
                                .centerCrop()
                                .override(300, 500)
                                .into(tvInfoPosterImageView)
                            tvInfoTitle.text = tvInfo.name
                            releaseDate.text = tvInfo.airDate?.replace("-", ".")
                            tvInfoOverview.text = tvInfo.overview
                            voteAverage.text =
                                getString(R.string.vote).format(tvInfo.voteAverage?.toOneScale())
                            voteCount.text = tvInfo.voteCount.toString()

                            tvInfoOverview.setOnClickListener {
                                MaterialAlertDialogBuilder(requireContext()).setMessage(tvInfo.overview)
                                    .show()
                            }

                            val tvCastAdapter = TvEpisodeCastAdapter(tvInfo, findNavController())
                            val directors = mutableListOf<String>()
                            val writers = mutableListOf<String>()
                            var tenCast = tvInfo.crew

                            while (tenCast?.size!! > 10) {
                                tenCast = tenCast.dropLast(1) as MutableList<Crew>?
                            }

                            tvInfo.apply {
                                crew = tenCast
                            }

                            tvInfoCastRecyclerView.apply {
                                adapter = tvCastAdapter
                                layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                            }
                            tvCastAdapter.differ.submitList(tvInfo.crew)

                            tvInfo.crew?.filter { it.department == "Directing" }
                                .also { it -> it?.forEach { directors.add(it.name!!) } }
                            tvInfo.crew?.filter { it.department == "Writing" }
                                .also { it -> it?.forEach { writers.add(it.name!!) } }

                            director.text =
                                getString(R.string.director).format(directors.joinToString())
                            writer.text = getString(R.string.writer).format(writers.joinToString())

                            tvInfoPosterImageView.setOnClickListener {
                                dialog.show(childFragmentManager, "PosterDialogFragment")
                            }
                        }
                        progressBar.visibility = View.GONE
                        tvInfoConstraintLayout.visibility = View.VISIBLE
                    }
                    Status.LOADING -> {
                        tvInfoConstraintLayout.visibility = View.GONE
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

        viewModel.getTvEpisodeVideos()
            .observe(viewLifecycleOwner) { it ->
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { videoItems ->
                            if (videoItems.results?.isEmpty() == true) {
                                videoCardView.visibility = View.GONE
                                progressBar.visibility = View.GONE
                            } else {
                                val videoAdapter = VideoAdapter(
                                    videoItems,
                                    findNavController()
                                )

                                val officialTrailer = videoItems.results?.find {
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
                                        .load(
                                            Constants.YOUTUBE_PREVIEW_URL.format(
                                                officialTrailer.key
                                            )
                                        )
                                        .placeholder(R.drawable.ic_placeholder)
                                        .centerCrop()
                                        .override(800, 600)
                                        .into(trailerPreview)

                                    trailerName.text = officialTrailer.name

                                    trailerCardView.setOnClickListener {
                                        findNavController().navigate(
                                            TvEpisodeFragmentDirections.actionTvEpisodeFragmentToVideoFragment(
                                                officialTrailer.key!!
                                            )
                                        )
                                    }
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

                                videoCardView.visibility = View.VISIBLE
                                progressBar.visibility = View.GONE
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

        viewModel.getTvEpisodeImages()
            .observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { imagesItems ->
                            if (imagesItems.stills.isNullOrEmpty()) {
                                imagesCardView.visibility = View.GONE
                                progressBar.visibility = View.GONE
                            } else {
                                val imageAdapter = EpisodeImagesAdapter(
                                    imagesItems,
                                )
                                var tenImages = imagesItems.stills

                                while (tenImages?.size!! > 10) {
                                    tenImages = tenImages.dropLast(1) as MutableList<Still>?
                                }

                                imagesItems.apply {
                                    stills = tenImages
                                }

                                imagesRecyclerView.apply {
                                    adapter = imageAdapter
                                    layoutManager =
                                        LinearLayoutManager(
                                            requireContext(),
                                            LinearLayoutManager.HORIZONTAL,
                                            false
                                        )
                                }
                                imageAdapter.differ.submitList(imagesItems.stills)

                                progressBar.visibility = View.GONE
                                imagesCardView.visibility = View.VISIBLE
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

        buttonSeeAllImages.setOnClickListener {
            val action =
                TvEpisodeFragmentDirections.actionTvEpisodeFragmentToImagesFragment()
            action.tvId = tvId.toString()

            findNavController().navigate(action)
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory(
                tvId = args.tvId,
                episodeNumber = args.episodeNumber,
                seasonNumber = args.seasonNumber
            )
        ).get(TvEpisodeViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}