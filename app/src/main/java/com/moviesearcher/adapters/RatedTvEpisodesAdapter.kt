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
import com.moviesearcher.api.entity.rated.tvepisode.RatedTvEpisodeResult
import com.moviesearcher.api.entity.rated.tvepisode.RatedTvEpisodesResponse

class RatedTvEpisodesAdapter(
    private val ratedTvEpisodeItems: RatedTvEpisodesResponse,
    private val navController: NavController
) : RecyclerView.Adapter<RatedTvEpisodesAdapter.RatedTvEpisodeViewHolder>() {

    class RatedTvEpisodeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ratedTvEpisodeItemPoster: ImageView =
            view.findViewById(R.id.image_view_rated_tv_item)
        private val ratedTvEpisodeItemName: TextView =
            view.findViewById(R.id.text_view_rated_tv_item_name)
        private val cardView: MaterialCardView =
            view.findViewById(R.id.material_card_view_rated_tv_item)

        fun bind(ratedTvEpisodeResultItem: RatedTvEpisodeResult) {

            cardView.id = ratedTvEpisodeResultItem.id?.toInt()!!
            ratedTvEpisodeItemName.text = ratedTvEpisodeResultItem.name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RatedTvEpisodeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_rated_tv_item, parent, false)
        val cardView: MaterialCardView =
            view.findViewById(R.id.material_card_view_rated_tv_item)

        cardView.setOnClickListener {
            val tvId = it.id

            TODO("navigate to tv episode details")
        }

        return RatedTvEpisodeViewHolder(view)
    }

    override fun getItemCount(): Int = ratedTvEpisodeItems.results?.size!!
    override fun onBindViewHolder(holder: RatedTvEpisodeViewHolder, position: Int) {
        val ratedTvEpisodeItem = ratedTvEpisodeItems.results?.get(position)
        holder.bind(ratedTvEpisodeItem!!)
    }
}