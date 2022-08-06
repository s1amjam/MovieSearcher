package com.moviesearcher.tv.seasons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.moviesearcher.common.utils.Constants.SEASON_NUMBER
import com.moviesearcher.common.utils.Constants.TV_ID
import com.moviesearcher.databinding.FragmentTvSeasonsBinding

class TvSeasonsFragment : Fragment() {
    private var _binding: FragmentTvSeasonsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<TvSeasonsFragmentArgs>()

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var episodesPagerAdapter: EpisodesPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvSeasonsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = binding.viewPager
        tabLayout = binding.tabLayout

        setupEpisodesAdapter()

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = (position + 1).toString()
        }.attach()
    }

    private fun setupEpisodesAdapter() {
        episodesPagerAdapter = EpisodesPagerAdapter(requireParentFragment())
        viewPager.adapter = episodesPagerAdapter
    }

    inner class EpisodesPagerAdapter(fragment: Fragment) :
        FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = args.numberOfSeasons

        override fun createFragment(position: Int): Fragment {
            val fragment = TvSeasonsViewPagerFragment()

            fragment.arguments = Bundle().apply {
                putLong(TV_ID, args.tvId)
                putString(SEASON_NUMBER, (position + 1).toString())
            }
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}