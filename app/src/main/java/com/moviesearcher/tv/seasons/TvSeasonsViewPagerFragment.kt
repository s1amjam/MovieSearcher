package com.moviesearcher.tv.seasons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.utils.Constants.ERROR_MESSAGE
import com.moviesearcher.common.utils.Constants.SEASON_NUMBER
import com.moviesearcher.common.utils.Constants.TV_ID
import com.moviesearcher.common.utils.Status
import com.moviesearcher.databinding.FragmentTvSeasonsViewPagerBinding
import com.moviesearcher.tv.episode.adapter.EpisodesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvSeasonsViewPagerFragment : Fragment() {
    private var _binding: FragmentTvSeasonsViewPagerBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<TvSeasonViewModel>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvSeasonsConstraintLayout: ConstraintLayout
    private lateinit var noEpisodesTv: TextView

    private lateinit var adapter: EpisodesAdapter

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

        setupAdapter()
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
                            adapter.submitList(episodeItems.episodes)

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

    private fun setupAdapter() {
        adapter = EpisodesAdapter(
            findNavController(),
            requireArguments()[SEASON_NUMBER] as String,
            requireArguments()[TV_ID] as Long
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}