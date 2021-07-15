package com.moviesearcher.rated.tv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.moviesearcher.R
import com.moviesearcher.rated.RatedFragmentDirections
import com.moviesearcher.rated.tv.model.RatedTvsResponse
import com.moviesearcher.rated.tv.model.RatedTvsResult
import com.moviesearcher.utils.Constants
import com.squareup.picasso.Picasso

class RatedTvsAdapter(
    private val ratedTvItems: RatedTvsResponse,
    private val navController: NavController
) : RecyclerView.Adapter<RatedTvsAdapter.RatedTvViewHolder>() {

    class RatedTvViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ratedTvItemPoster: ImageView =
            view.findViewById(R.id.image_view_rated_tv_item)
        private val ratedTvItemName: TextView =
            view.findViewById(R.id.text_view_rated_tv_item_name)
        private val cardView: MaterialCardView =
            view.findViewById(R.id.material_card_view_rated_tv_item)

        fun bind(ratedTvResultItem: RatedTvsResult) {
            Picasso.get()
                .load(Constants.IMAGE_URL + ratedTvResultItem.posterPath)
                .into(ratedTvItemPoster)

            cardView.id = ratedTvResultItem.id?.toInt()!!
            ratedTvItemName.text = ratedTvResultItem.name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RatedTvViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_rated_tv_item, parent, false)
        val cardView: MaterialCardView =
            view.findViewById(R.id.material_card_view_rated_tv_item)

        cardView.setOnClickListener {
            val tvId = it.id.toLong()

            navController.navigate(
                RatedFragmentDirections.actionRatedFragmentToTvInfoFragment(
                    tvId
                )
            )
        }

        return RatedTvViewHolder(view)
    }

    override fun getItemCount(): Int = ratedTvItems.results?.size!!
    override fun onBindViewHolder(holder: RatedTvViewHolder, position: Int) {
        val ratedTvItem = ratedTvItems.results?.get(position)
        holder.bind(ratedTvItem!!)
    }
}