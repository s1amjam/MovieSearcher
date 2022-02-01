package com.moviesearcher.tv.seasons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.common.viewmodel.BaseViewModel
import com.moviesearcher.databinding.FragmentTvSeasonsViewPagerBinding
import com.moviesearcher.tv.episode.adapter.EpisodesAdapter

private const val TAG = "TvSeasonsViewPagerFragment"

class TvSeasonsViewPagerFragment : BaseFragment() {
    private var _binding: FragmentTvSeasonsViewPagerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BaseViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvSeasonsConstraintLayout: ConstraintLayout

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

        progressBar.visibility = View.VISIBLE
        tvSeasonsConstraintLayout.visibility = View.INVISIBLE

        viewModel.getTvSeason(
            requireArguments()["id"] as Long?,
            requireArguments()["seasonNumber"] as String?
        ).observe(viewLifecycleOwner) { episodeItems ->
                val episodesAdapter = EpisodesAdapter(
                    episodeItems.episodes!!, findNavController(),
                    requireArguments()["seasonNumber"] as String?,
                    requireArguments()["id"] as Long?
                )

                recyclerView.apply {
                    adapter = episodesAdapter
                    layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                }
                episodesAdapter.differ.submitList(episodeItems.episodes)

                progressBar.visibility = View.GONE
                tvSeasonsConstraintLayout.visibility = View.VISIBLE
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}