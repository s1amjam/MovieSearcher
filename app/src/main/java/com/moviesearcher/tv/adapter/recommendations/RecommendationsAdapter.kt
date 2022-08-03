package com.moviesearcher.tv.adapter.recommendations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.extensions.loadImage
import com.moviesearcher.common.extensions.toOneScale
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.TvCardViewBinding
import com.moviesearcher.favorite.tv.model.FavoriteTvResponse
import com.moviesearcher.favorite.tv.model.ResultFavoriteTv
import com.moviesearcher.tv.TvInfoFragmentDirections

class TvRecommendationsAdapter(
    private val recommendationsItems: FavoriteTvResponse,
    private val navController: NavController,
) : RecyclerView.Adapter<TvRecommendationsAdapter.TvRecommendationsHolder>() {
    private lateinit var binding: TvCardViewBinding

    inner class TvRecommendationsHolder(binding: TvCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate
        private val cardView = binding.trendingCardView
        private val posterImageView = binding.posterImageView

        fun bind(recommendationsItem: ResultFavoriteTv) {
            title.text = recommendationsItem.name
            releaseDate.text = recommendationsItem.firstAirDate?.replace("-", ".")
            rating.text = recommendationsItem.voteAverage?.toOneScale()
            posterImageView.loadImage(
                Constants.IMAGE_URL + recommendationsItem.posterPath,
                isCardView = true
            )

            cardView.setOnClickListener {
                navController.navigate(
                    TvInfoFragmentDirections.actionTvInfoFragmentSelf(recommendationsItem.id!!)
                )
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TvRecommendationsAdapter.TvRecommendationsHolder {
        binding = TvCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TvRecommendationsHolder(binding)
    }

    private val differCallback = object : DiffUtil.ItemCallback<ResultFavoriteTv>() {
        override fun areItemsTheSame(
            oldItem: ResultFavoriteTv,
            newItem: ResultFavoriteTv
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResultFavoriteTv,
            newItem: ResultFavoriteTv
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int = recommendationsItems.results?.size!!
    override fun onBindViewHolder(
        holder: TvRecommendationsAdapter.TvRecommendationsHolder,
        position: Int
    ) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }
}