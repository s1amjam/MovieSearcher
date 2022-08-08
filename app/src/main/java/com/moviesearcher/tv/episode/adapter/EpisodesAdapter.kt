package com.moviesearcher.tv.episode.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.extensions.loadImage
import com.moviesearcher.common.extensions.toOneScale
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.ExtendedCardViewBinding
import com.moviesearcher.tv.seasons.TvSeasonsFragmentDirections
import com.moviesearcher.tv.seasons.model.Episode

class EpisodesAdapter(
    private val navController: NavController,
    private val seasonNumber: String,
    private val tvId: Long,
) : ListAdapter<Episode, EpisodesAdapter.TvEpisodeViewHolder>(
    AsyncDifferConfig.Builder(
        DiffCallback()
    ).build()
) {

    inner class TvEpisodeViewHolder(private val binding: ExtendedCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cardView.setOnClickListener {
                currentList[adapterPosition].episodeNumber?.let { it1 ->
                    navController.navigate(
                        TvSeasonsFragmentDirections.actionTvSeasonsFragmentToTvEpisodeFragment(
                            tvId,
                            seasonNumber,
                            it1
                        )
                    )
                }
            }
        }

        fun bind(tvEpisodeItem: Episode) {
            binding.apply {
                imageButtonRemove.visibility = View.GONE
                posterImageView.loadImage(
                    Constants.IMAGE_URL + tvEpisodeItem.stillPath,
                    isCardView = true
                )
                textViewTitle.text = tvEpisodeItem.name
                textViewRating.text = tvEpisodeItem.voteAverage?.toOneScale()
                textViewDescription.text = tvEpisodeItem.overview
                textViewReleaseDate.text = tvEpisodeItem.airDate
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EpisodesAdapter.TvEpisodeViewHolder {
        return TvEpisodeViewHolder(
            ExtendedCardViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EpisodesAdapter.TvEpisodeViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    private class DiffCallback : DiffUtil.ItemCallback<Episode>() {
        override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem.id == newItem.id;
        }

        override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem == newItem
        }
    }
}