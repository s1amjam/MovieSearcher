package com.moviesearcher.tv.episode.adapter.cast

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.R
import com.moviesearcher.databinding.MovieCastItemBinding
import com.moviesearcher.tv.episode.TvEpisodeFragmentDirections
import com.moviesearcher.tv.episode.model.Crew
import com.moviesearcher.tv.episode.model.TvEpisodeResponse
import com.moviesearcher.utils.Constants

class TvEpisodeCastAdapter(
    private val castItems: TvEpisodeResponse,
    private val navController: NavController
) : RecyclerView.Adapter<TvEpisodeCastAdapter.TvEpisodeCastHolder>() {
    private lateinit var binding: MovieCastItemBinding

    inner class TvEpisodeCastHolder(binding: MovieCastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val name = binding.textViewName
        private val characterName = binding.textViewCharacterName
        private val poster: ImageView = binding.posterImageView
        private val roles = mutableListOf<String>()
        private val castCardView: CardView = binding.castCardView

        fun bind(castItem: Crew) {
            name.text = castItem.name
            characterName.text = roles.joinToString()

            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + castItem.profilePath)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .override(400, 600)
                .into(poster)

            castCardView.setOnClickListener {
                navController.navigate(
                    TvEpisodeFragmentDirections.actionTvEpisodeFragmentToPersonInfoFragment(
                        castItem.id?.toLong()!!
                    )
                )
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Crew>() {
        override fun areItemsTheSame(oldItem: Crew, newItem: Crew): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Crew, newItem: Crew): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TvEpisodeCastAdapter.TvEpisodeCastHolder {
        binding = MovieCastItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TvEpisodeCastHolder(binding)
    }

    override fun getItemCount(): Int = castItems.crew?.size!!
    override fun onBindViewHolder(holder: TvEpisodeCastAdapter.TvEpisodeCastHolder, position: Int) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }
}