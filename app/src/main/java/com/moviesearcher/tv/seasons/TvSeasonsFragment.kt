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
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.databinding.FragmentTvSeasonsBinding

private const val TAG = "TvSeasonsFragment"

class TvSeasonsFragment : BaseFragment() {
    private var _binding: FragmentTvSeasonsBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<TvSeasonsFragmentArgs>()

    private lateinit var tabLayout: TabLayout
    private lateinit var episodesViewPager: ViewPager2
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

        val tvId = args.tvId
        var numberOfSeasons = args.numberOfSeasons
        var seasonNumber = 1

        episodesViewPager = binding.episodesViewPager
        tabLayout = binding.tabLayout

        while (numberOfSeasons > 0) {
            tabLayout.addTab(tabLayout.newTab().setText(seasonNumber.toString()))

            seasonNumber++
            numberOfSeasons--
        }
        //tabLayout.setupWithViewPager(episodesViewPager)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                setupEpisodesUi(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.position
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                setupEpisodesUi(tab)
            }
        })

        tabLayout.getTabAt(0)?.select()
    }

    private fun setupEpisodesUi(tab: TabLayout.Tab?) {
        episodesPagerAdapter =
            EpisodesPagerAdapter(requireParentFragment(), (tabLayout.selectedTabPosition + 1).toString())
        episodesViewPager.adapter = episodesPagerAdapter
        episodesViewPager.currentItem = tab?.position!!
    }

    inner class EpisodesPagerAdapter(fragment: Fragment, private val seasonNumber: String) :
        FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 100

        override fun createFragment(position: Int): Fragment {
            val fragment = TvSeasonsViewPagerFragment()

            fragment.arguments = Bundle().apply {
                putLong("id", args.tvId)
                putString("seasonNumber", seasonNumber)
            }
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}