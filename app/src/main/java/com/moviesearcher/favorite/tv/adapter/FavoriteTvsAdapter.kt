package com.moviesearcher.favorite.tv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.android.material.card.MaterialCardView
import com.moviesearcher.databinding.FragmentFavoriteTvItemBinding
import com.moviesearcher.favorite.FavoritesFragmentDirections
import com.moviesearcher.favorite.tv.model.FavoriteTvResponse
import com.moviesearcher.favorite.tv.model.ResultFavoriteTv
import com.moviesearcher.utils.Constants

class FavoriteTvsAdapter(
    private val favoriteTvsItems: FavoriteTvResponse,
    private val navController: NavController
) : RecyclerView.Adapter<FavoriteTvsAdapter.FavoriteTvViewHolder>() {
    lateinit var cardView: MaterialCardView

    inner class FavoriteTvViewHolder(binding: FragmentFavoriteTvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val favoriteTvsItemPoster: ImageView = binding.imageViewFavoriteTvItem
        private val favoriteTvsItemName: TextView = binding.textViewFavoriteTvItemName

        fun bind(favoriteTvsResultItem: ResultFavoriteTv) {
            Glide.with(this.itemView)
                .load(Constants.IMAGE_URL + favoriteTvsResultItem.posterPath)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(favoriteTvsItemPoster)
            cardView.id = favoriteTvsResultItem.id?.toInt()!!
            favoriteTvsItemName.text = favoriteTvsResultItem.name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteTvViewHolder {
        val binding = FragmentFavoriteTvItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        cardView = binding.materialCardViewFavoriteTvItem

        cardView.setOnClickListener {
            val tvId = it.id

            navController.navigate(
                FavoritesFragmentDirections.actionFragmentFavoritesToTvInfoFragment(
                    tvId.toLong()
                )
            )
        }

        return FavoriteTvViewHolder(binding)
    }

    override fun getItemCount(): Int = favoriteTvsItems.results?.size!!
    override fun onBindViewHolder(holder: FavoriteTvViewHolder, position: Int) {
        val favoriteTvsItem = favoriteTvsItems.results?.get(position)
        holder.bind(favoriteTvsItem!!)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}