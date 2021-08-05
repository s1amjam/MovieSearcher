package com.moviesearcher.watchlist.tv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.android.material.card.MaterialCardView
import com.moviesearcher.databinding.FragmentTvWatchlistItemBinding
import com.moviesearcher.utils.Constants
import com.moviesearcher.watchlist.WatchlistFragmentDirections
import com.moviesearcher.watchlist.tv.model.TvWatchlistResponse
import com.moviesearcher.watchlist.tv.model.TvWatchlistResult

class TvWatchlistAdapter(
    private val tvWatchlistItems: TvWatchlistResponse,
    private val navController: NavController
) : RecyclerView.Adapter<TvWatchlistAdapter.TvWatchlistViewHolder>() {
    lateinit var cardView: MaterialCardView

    inner class TvWatchlistViewHolder(binding: FragmentTvWatchlistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val tvWatchlistItemPoster: ImageView = binding.imageViewTvWatchlistItem
        private val tvWatchlistItemName: TextView = binding.textViewTvWatchlistItemName

        fun bind(tvWatchlistResultItem: TvWatchlistResult) {
            Glide.with(this.itemView)
                .load(Constants.IMAGE_URL + tvWatchlistResultItem.posterPath)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(tvWatchlistItemPoster)
            cardView.id = tvWatchlistResultItem.id?.toInt()!!
            tvWatchlistItemName.text = tvWatchlistResultItem.name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TvWatchlistViewHolder {
        val binding = FragmentTvWatchlistItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        cardView = binding.materialCardViewTvWatchlistItem

        cardView.setOnClickListener {
            val tvId = it.id.toLong()

            navController.navigate(
                WatchlistFragmentDirections.actionWatchlistFragmentToTvInfoFragment(
                    tvId
                )
            )
        }

        return TvWatchlistViewHolder(binding)
    }

    override fun getItemCount(): Int = tvWatchlistItems.results?.size!!
    override fun onBindViewHolder(holder: TvWatchlistViewHolder, position: Int) {
        val tvWatchlistItem = tvWatchlistItems.results?.get(position)
        holder.bind(tvWatchlistItem!!)
    }
}