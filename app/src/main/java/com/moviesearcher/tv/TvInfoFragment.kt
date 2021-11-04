package com.moviesearcher.tv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.moviesearcher.R
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.databinding.FragmentTvInfoBinding
import com.moviesearcher.list.lists.viewmodel.MyListsViewModel
import com.moviesearcher.tv.viewmodel.TvInfoViewModel
import com.moviesearcher.utils.Constants

private const val TAG = "TvInfoFragment"

class TvInfoFragment : BaseFragment() {
    private var _binding: FragmentTvInfoBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<TvInfoFragmentArgs>()
    private val tvInfoViewModel: TvInfoViewModel by viewModels()
    private val myLists: MyListsViewModel by viewModels()

    private lateinit var tvInfoPosterImageView: ImageView
    private lateinit var tvInfoName: TextView
    private lateinit var tvInfoGenres: TextView
    private lateinit var tvInfoProductionCountries: TextView
    private lateinit var tvInfoTagline: TextView
    private lateinit var tvInfoFirstAirDate: TextView
    private lateinit var tvInfoOverview: TextView
    private lateinit var tvInfoConstraintLayout: ConstraintLayout
    private lateinit var menuButtonAddToList: Button
    private lateinit var buttonMarkTvAsFavorite: Button
    private lateinit var buttonWatchlist: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvInfoBinding.inflate(inflater, container, false)
        val view = binding.root
        val tvId = args.tvId

        tvInfoConstraintLayout = binding.tvInfoConstraintLayout
        tvInfoPosterImageView = binding.tvInfoPosterImageView
        tvInfoName = binding.tvInfoName
        tvInfoGenres = binding.tvInfoGenres
        tvInfoProductionCountries = binding.tvInfoProductionCountries
        tvInfoTagline = binding.tvInfoTagline
        tvInfoFirstAirDate = binding.tvInfoFirstAirDate
        tvInfoOverview = binding.tvInfoOverview
        menuButtonAddToList = binding.menuButtonAddTvToList
        buttonMarkTvAsFavorite = binding.buttonMarkTvAsFavorite
        buttonWatchlist = binding.buttonWatchlist

        menuButtonAddToList.isVisible = sessionId != ""
        buttonMarkTvAsFavorite.isVisible = sessionId != ""
        buttonWatchlist.isVisible = sessionId != ""

        tvInfoViewModel.getTvInfo(tvId).observe(
            viewLifecycleOwner,
            { tvInfo ->
                Glide.with(this)
                    .load(Constants.IMAGE_URL + tvInfo?.posterPath)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .into(tvInfoPosterImageView)
                tvInfoName.text = tvInfo?.name
                tvInfoGenres.text = tvInfo?.genres?.joinToString { genre -> genre.name!! }
                tvInfoProductionCountries.text =
                    tvInfo?.productionCountries?.joinToString { productionCountry ->
                        productionCountry.name!!
                    }
                tvInfoTagline.text = tvInfo?.tagline
                tvInfoFirstAirDate.text = tvInfo?.firstAirDate
                tvInfoOverview.text = tvInfo?.overview
            })

        menuButtonAddToList.setOnClickListener { v ->
            myLists.getLists(accountId, sessionId, 1).observe(viewLifecycleOwner, {
                showAddToListMenu(v, R.menu.list_popup_menu, it.results!!)
            })
        }

        //checkFavorites(buttonMarkTvAsFavorite)

//        buttonMarkTvAsFavorite.setOnClickListener {
//            markAsFavorite(buttonMarkTvAsFavorite)
//        }

        //checkWatchlist(buttonWatchlist)

        buttonWatchlist.setOnClickListener {
            //addToWatchlist(buttonWatchlist)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}