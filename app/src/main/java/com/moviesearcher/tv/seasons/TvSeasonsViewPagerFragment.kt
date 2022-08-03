package com.moviesearcher.tv.seasons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.common.utils.Status
import com.moviesearcher.common.viewmodel.ViewModelFactory
import com.moviesearcher.databinding.FragmentTvSeasonsViewPagerBinding
import com.moviesearcher.tv.episode.adapter.EpisodesAdapter
import com.moviesearcher.tv.seasons.model.TvSeasonResponse

private const val TAG = "TvSeasonsViewPagerFragment"

class TvSeasonsViewPagerFragment : BaseFragment() {
    private var _binding: FragmentTvSeasonsViewPagerBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TvSeasonViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvSeasonsConstraintLayout: ConstraintLayout
    private lateinit var noEpisodesTv: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvSeasonsViewPagerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.tvSeasonsRecyclerView
        tvSeasonsConstraintLayout = binding.tvSeasonsRecyclerViewConstraintLayout
        progressBar = binding.tvSeasonsProgressBar
        noEpisodesTv = binding.noEpisodesTv

        setupViewModel()
        setupUi()
    }

    private fun setupUi() {
        viewModel.getTvSeason().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { episodeItems ->
                        if (episodeItems.episodes?.isEmpty() == true) {
                            noEpisodesTv.visibility = View.VISIBLE
                        } else {
                            setupSeasonsUi(episodeItems)

                            progressBar.visibility = View.GONE
                            tvSeasonsConstraintLayout.visibility = View.VISIBLE
                        }
                        progressBar.visibility = View.GONE
                    }
                }
                Status.LOADING -> {
                    tvSeasonsConstraintLayout.visibility = View.GONE
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
    }

    private fun setupSeasonsUi(episodeItems: TvSeasonResponse) {
        val episodesAdapter = EpisodesAdapter(
            episodeItems.episodes!!, findNavController(),
            requireArguments()["seasonNumber"] as String,
            requireArguments()["id"] as Long
        )

        recyclerView.apply {
            adapter = episodesAdapter
            layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
        }
        episodesAdapter.differ.submitList(episodeItems.episodes)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory(
                tvId = requireArguments()["id"] as Long,
                seasonNumber = requireArguments()["seasonNumber"] as String
            )
        ).get(TvSeasonViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}