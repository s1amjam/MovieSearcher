package com.moviesearcher.tv.adapter.recommendations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.R
import com.moviesearcher.databinding.TvCardViewBinding
import com.moviesearcher.favorite.tv.model.FavoriteTvResponse
import com.moviesearcher.favorite.tv.model.ResultFavoriteTv
import com.moviesearcher.tv.TvInfoFragmentDirections
import com.moviesearcher.utils.Constants

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
            rating.text = recommendationsItem.getAverage()

            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + recommendationsItem.posterPath)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .override(400, 600)
                .into(posterImageView)

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