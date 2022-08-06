package com.moviesearcher.tv.episode.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.extensions.loadImage
import com.moviesearcher.common.extensions.toOneScale
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.ExtendedCardViewBinding
import com.moviesearcher.tv.seasons.TvSeasonsFragmentDirections
import com.moviesearcher.tv.seasons.model.Episode

class EpisodesAdapter(
    private val episodeItems: List<Episode>,
    private val navController: NavController,
    private val seasonNumber: String,
    private val tvId: Long,
) : RecyclerView.Adapter<EpisodesAdapter.TvEpisodeViewHolder>() {

    inner class TvEpisodeViewHolder(binding: ExtendedCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val title: TextView = binding.textViewTitle
        private val rating = binding.textViewRating
        private val releaseDate = binding.textViewReleaseDate
        private val overview = binding.textViewDescription
        private val poster = binding.posterImageView
        private val cardView = binding.cardView
        private val removeButton = binding.imageButtonRemove

        fun bind(tvEpisodeItem: Episode) {
            removeButton.visibility = View.GONE
            poster.loadImage(Constants.IMAGE_URL + tvEpisodeItem.stillPath, isCardView = true)
            title.text = tvEpisodeItem.name
            tvEpisodeItem.airDate
            rating.text = tvEpisodeItem.voteAverage?.toOneScale()
            overview.text = tvEpisodeItem.overview
            releaseDate.text = tvEpisodeItem.airDate

            cardView.setOnClickListener {
                navController.navigate(
                    TvSeasonsFragmentDirections.actionTvSeasonsFragmentToTvEpisodeFragment(
                        tvId,
                        seasonNumber,
                        tvEpisodeItem.episodeNumber!!
                    )
                )
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Episode>() {
        override fun areItemsTheSame(
            oldItem: Episode,
            newItem: Episode
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Episode,
            newItem: Episode
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EpisodesAdapter.TvEpisodeViewHolder {
        val binding = ExtendedCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TvEpisodeViewHolder(binding)
    }

    override fun getItemCount(): Int = episodeItems.size
    override fun onBindViewHolder(holder: EpisodesAdapter.TvEpisodeViewHolder, position: Int) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }
}