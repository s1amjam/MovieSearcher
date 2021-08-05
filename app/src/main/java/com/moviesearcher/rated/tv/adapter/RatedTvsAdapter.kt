package com.moviesearcher.rated.tv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.android.material.card.MaterialCardView
import com.moviesearcher.databinding.FragmentRatedTvItemBinding
import com.moviesearcher.rated.RatedFragmentDirections
import com.moviesearcher.rated.tv.model.RatedTvsResponse
import com.moviesearcher.rated.tv.model.RatedTvsResult
import com.moviesearcher.utils.Constants

class RatedTvsAdapter(
    private val ratedTvItems: RatedTvsResponse,
    private val navController: NavController
) : RecyclerView.Adapter<RatedTvsAdapter.RatedTvViewHolder>() {
    lateinit var cardView: MaterialCardView

    inner class RatedTvViewHolder(binding: FragmentRatedTvItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val ratedTvItemPoster: ImageView = binding.imageViewRatedTvItem
        private val ratedTvItemName: TextView = binding.textViewRatedTvItemName

        fun bind(ratedTvResultItem: RatedTvsResult) {
            Glide.with(this.itemView)
                .load(Constants.IMAGE_URL + ratedTvResultItem.posterPath)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(ratedTvItemPoster)
            cardView.id = ratedTvResultItem.id?.toInt()!!
            ratedTvItemName.text = ratedTvResultItem.name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RatedTvViewHolder {
        val binding = FragmentRatedTvItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        cardView = binding.materialCardViewRatedTvItem

        cardView.setOnClickListener {
            val tvId = it.id.toLong()

            navController.navigate(
                RatedFragmentDirections.actionRatedFragmentToTvInfoFragment(
                    tvId
                )
            )
        }

        return RatedTvViewHolder(binding)
    }

    override fun getItemCount(): Int = ratedTvItems.results?.size!!
    override fun onBindViewHolder(holder: RatedTvViewHolder, position: Int) {
        val ratedTvItem = ratedTvItems.results?.get(position)
        holder.bind(ratedTvItem!!)
    }
}