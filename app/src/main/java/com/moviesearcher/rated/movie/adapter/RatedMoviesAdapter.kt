package com.moviesearcher.rated.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.android.material.card.MaterialCardView
import com.moviesearcher.databinding.FragmentRatedMovieItemBinding
import com.moviesearcher.rated.RatedFragmentDirections
import com.moviesearcher.rated.movie.model.RatedMoviesResponse
import com.moviesearcher.rated.movie.model.RatedMoviesResult
import com.moviesearcher.common.utils.Constants

class RatedMoviesAdapter(
    private val ratedMovieItems: RatedMoviesResponse,
    private val navController: NavController
) : RecyclerView.Adapter<RatedMoviesAdapter.RatedMovieViewHolder>() {
    lateinit var cardView: MaterialCardView

    inner class RatedMovieViewHolder(binding: FragmentRatedMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val ratedMovieItemPoster: ImageView = binding.imageViewRatedMovieItem
        private val ratedMovieItemName: TextView = binding.textViewRatedMovieItemName
        private val cardView = binding.materialCardViewRatedMovieItem

        fun bind(ratedMovieResultItem: RatedMoviesResult) {
            Glide.with(this.itemView)
                .load(Constants.IMAGE_URL + ratedMovieResultItem.posterPath)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(ratedMovieItemPoster)
            cardView.id = ratedMovieResultItem.id?.toInt()!!
            ratedMovieItemName.text = ratedMovieResultItem.title

            cardView.setOnClickListener {
                navController.navigate(
                    RatedFragmentDirections.actionRatedFragmentToMovieInfoFragment(
                        ratedMovieResultItem.id
                    )
                )
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<RatedMoviesResult>() {
        override fun areItemsTheSame(
            oldItem: RatedMoviesResult,
            newItem: RatedMoviesResult
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RatedMoviesResult,
            newItem: RatedMoviesResult
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RatedMovieViewHolder {
        val binding = FragmentRatedMovieItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return RatedMovieViewHolder(binding)
    }

    override fun getItemCount(): Int = ratedMovieItems.results?.size!!
    override fun onBindViewHolder(holder: RatedMovieViewHolder, position: Int) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }
}