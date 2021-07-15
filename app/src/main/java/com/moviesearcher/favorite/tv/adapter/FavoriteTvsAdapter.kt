package com.moviesearcher.favorite.tv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.moviesearcher.R
import com.moviesearcher.favorite.FavoritesFragmentDirections
import com.moviesearcher.favorite.tv.model.FavoriteTvResponse
import com.moviesearcher.favorite.tv.model.ResultFavoriteTv
import com.moviesearcher.utils.Constants
import com.squareup.picasso.Picasso

class FavoriteTvsAdapter(
    private val favoriteTvsItems: FavoriteTvResponse,
    private val navController: NavController
) : RecyclerView.Adapter<FavoriteTvsAdapter.FavoriteTvViewHolder>() {

    class FavoriteTvViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val favoriteTvsItemPoster: ImageView =
            view.findViewById(R.id.image_view_favorite_tv_item)
        private val favoriteTvsItemName: TextView =
            view.findViewById(R.id.text_view_favorite_tv_item_name)
        private val cardView: MaterialCardView =
            view.findViewById(R.id.material_card_view_favorite_tv_item)

        fun bind(favoriteTvsResultItem: ResultFavoriteTv) {
            Picasso.get()
                .load(Constants.IMAGE_URL + favoriteTvsResultItem.posterPath)
                .into(favoriteTvsItemPoster)

            cardView.id = favoriteTvsResultItem.id?.toInt()!!
            favoriteTvsItemName.text = favoriteTvsResultItem.name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteTvViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_favorite_tv_item, parent, false)
        val cardView: MaterialCardView =
            view.findViewById(R.id.material_card_view_favorite_tv_item)

        cardView.setOnClickListener {
            val tvId = it.id

            navController.navigate(
                FavoritesFragmentDirections.actionFragmentFavoritesToTvInfoFragment(
                    tvId.toLong()
                )
            )
        }

        return FavoriteTvViewHolder(view)
    }

    override fun getItemCount(): Int = favoriteTvsItems.results?.size!!
    override fun onBindViewHolder(holder: FavoriteTvViewHolder, position: Int) {
        val favoriteTvsItem = favoriteTvsItems.results?.get(position)
        holder.bind(favoriteTvsItem!!)
    }
}