package com.moviesearcher.favorite.tv.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.R
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.ExtendedCardViewBinding
import com.moviesearcher.favorite.FavoriteViewModel
import com.moviesearcher.favorite.FavoritesFragmentDirections
import com.moviesearcher.favorite.tv.model.FavoriteTvResponse
import com.moviesearcher.favorite.tv.model.ResultFavoriteTv

class FavoriteTvAdapter(
    private val favoriteTvItems: FavoriteTvResponse,
    private val navController: NavController,
    private val favoriteViewModel: FavoriteViewModel,
    private val context: Context,
) : RecyclerView.Adapter<FavoriteTvAdapter.FavoriteTvViewHolder>() {
    inner class FavoriteTvViewHolder(val binding: ExtendedCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate
        private val poster = binding.posterImageView
        private val overview = binding.textViewDescription
        private val imageButtonRemoveFromFavorite = binding.imageButtonRemove
        private val cardView = binding.cardView

        fun bind(tvItem: ResultFavoriteTv) {
            val mediaInfo: MutableMap<String, Long> = mutableMapOf()

            title.text = tvItem.name
            releaseDate.text = tvItem.firstAirDate?.replace("-", ".")
            imageButtonRemoveFromFavorite.setImageResource(R.drawable.ic_round_star_filled_36)

            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + tvItem.posterPath)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .override(400, 600)
                .into(poster)

            overview.text = tvItem.overview
            rating.text = tvItem.getAverage()
            cardView.id = tvItem.id?.toInt()!!

            mediaInfo["tv"] = cardView.id.toLong()

            imageButtonRemoveFromFavorite.setOnClickListener {
                favoriteViewModel.markAsFavorite(
                    imageButtonRemoveFromFavorite,
                    binding.root.findViewTreeLifecycleOwner()!!,
                    mediaInfo,
                    context,
                )
            }

            cardView.setOnClickListener {
                navController.navigate(
                    FavoritesFragmentDirections.actionFragmentFavoritesToTvInfoFragment(tvItem.id)
                )
            }
        }
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteTvViewHolder {
        val binding = ExtendedCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return FavoriteTvViewHolder(binding)
    }

    override fun getItemCount(): Int = favoriteTvItems.results?.size!!
    override fun onBindViewHolder(holder: FavoriteTvViewHolder, position: Int) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }
}