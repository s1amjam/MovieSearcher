package com.moviesearcher.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.moviesearcher.R
import com.moviesearcher.WatchlistFragmentDirections
import com.moviesearcher.api.entity.watchlist.tv.TvWatchlistResponse
import com.moviesearcher.api.entity.watchlist.tv.TvWatchlistResult
import com.moviesearcher.utils.Constants
import com.squareup.picasso.Picasso

class TvWatchlistAdapter(
    private val tvWatchlistItems: TvWatchlistResponse,
    private val navController: NavController
) : RecyclerView.Adapter<TvWatchlistAdapter.TvWatchlistViewHolder>() {

    class TvWatchlistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvWatchlistItemPoster: ImageView =
            view.findViewById(R.id.image_view_tv_watchlist_item)
        private val tvWatchlistItemName: TextView =
            view.findViewById(R.id.text_view_tv_watchlist_item_name)
        private val cardView: MaterialCardView =
            view.findViewById(R.id.material_card_view_tv_watchlist_item)

        fun bind(tvWatchlistResultItem: TvWatchlistResult) {
            Picasso.get()
                .load(Constants.IMAGE_URL + tvWatchlistResultItem.posterPath)
                .into(tvWatchlistItemPoster)

            cardView.id = tvWatchlistResultItem.id?.toInt()!!
            tvWatchlistItemName.text = tvWatchlistResultItem.name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TvWatchlistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_tv_watchlist_item, parent, false)
        val cardView: MaterialCardView =
            view.findViewById(R.id.material_card_view_tv_watchlist_item)

        cardView.setOnClickListener {
            val tvId = it.id.toLong()

            navController.navigate(
                WatchlistFragmentDirections.actionWatchlistFragmentToTvInfoFragment(
                    tvId
                )
            )
        }

        return TvWatchlistViewHolder(view)
    }

    override fun getItemCount(): Int = tvWatchlistItems.results?.size!!
    override fun onBindViewHolder(holder: TvWatchlistViewHolder, position: Int) {
        val tvWatchlistItem = tvWatchlistItems.results?.get(position)
        holder.bind(tvWatchlistItem!!)
    }
}