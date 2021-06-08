package com.moviesearcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.moviesearcher.utils.Constants
import com.moviesearcher.viewmodel.TvInfoViewModel
import com.squareup.picasso.Picasso

private const val TAG = "TvInfoFragment"

class TvInfoFragment : Fragment() {
    private val args by navArgs<TvInfoFragmentArgs>()
    private lateinit var tvInfoViewModel: TvInfoViewModel
    private lateinit var tvInfoPosterImageView: ImageView
    private lateinit var tvInfoName: TextView
    private lateinit var tvInfoGenres: TextView
    private lateinit var tvInfoProductionCountries: TextView
    private lateinit var tvInfoTagline: TextView
    private lateinit var tvInfoFirstAirDate: TextView
    private lateinit var tvInfoOverview: TextView
    private lateinit var tvInfoConstraintLayout: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tv_info, container, false)
        val tvId = args.tvId
        tvInfoConstraintLayout = view.findViewById(R.id.tv_info_constraint_layout)
        tvInfoViewModel = TvInfoViewModel(tvId)
        tvInfoPosterImageView = view.findViewById(R.id.tv_info_poster_image_view)
        tvInfoName = view.findViewById(R.id.tv_info_name)
        tvInfoGenres = view.findViewById(R.id.tv_info_genres)
        tvInfoProductionCountries = view.findViewById(R.id.tv_info_production_countries)
        tvInfoTagline = view.findViewById(R.id.tv_info_tagline)
        tvInfoFirstAirDate = view.findViewById(R.id.tv_info_first_air_date)
        tvInfoOverview = view.findViewById(R.id.tv_info_overview)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvInfoViewModel.tvInfoLiveData.observe(
            viewLifecycleOwner,
            { tvInfo ->
                Picasso.get()
                    .load(Constants.IMAGE_URL + tvInfo?.posterPath)
                    .into(tvInfoPosterImageView)
                tvInfoName.text = tvInfo?.name
                tvInfoGenres.text =
                    tvInfo?.genres?.joinToString { genre -> genre.name!! }
                tvInfoProductionCountries.text =
                    tvInfo?.productionCountries?.joinToString { productionCountry ->
                        productionCountry.name!!
                    }
                tvInfoTagline.text = tvInfo?.tagline
                tvInfoFirstAirDate.text = tvInfo?.firstAirDate
                tvInfoOverview.text = tvInfo?.overview
            })
    }
}