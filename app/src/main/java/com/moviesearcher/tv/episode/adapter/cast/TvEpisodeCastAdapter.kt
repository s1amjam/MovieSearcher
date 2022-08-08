package com.moviesearcher.tv.episode.adapter.cast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.extensions.loadImage
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.MovieCastItemBinding
import com.moviesearcher.tv.episode.TvEpisodeFragmentDirections
import com.moviesearcher.tv.episode.model.Crew

class TvEpisodeCastAdapter(
    private val navController: NavController
) : ListAdapter<Crew, TvEpisodeCastAdapter.TvEpisodeCastHolder>(
    AsyncDifferConfig.Builder(
        DiffCallback()
    ).build()
) {

    inner class TvEpisodeCastHolder(private val binding: MovieCastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val roles = mutableListOf<String>()

        init {
            binding.castCardView.setOnClickListener {
                currentList[adapterPosition].id?.let { it1 ->
                    navController.navigate(
                        TvEpisodeFragmentDirections.actionTvEpisodeFragmentToPersonInfoFragment(
                            it1.toLong()
                        )
                    )
                }
            }
        }

        fun bind(castItem: Crew) {
            binding.apply {
                textViewName.text = castItem.name
                textViewCharacterName.text = roles.joinToString()
                posterImageView.loadImage(
                    Constants.IMAGE_URL + castItem.profilePath,
                    isCardView = true
                )
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TvEpisodeCastAdapter.TvEpisodeCastHolder {
        return TvEpisodeCastHolder(
            MovieCastItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TvEpisodeCastAdapter.TvEpisodeCastHolder, position: Int) {
        holder.bind(currentList[position])
    }

    private class DiffCallback : DiffUtil.ItemCallback<Crew>() {
        override fun areItemsTheSame(
            oldItem: Crew,
            newItem: Crew
        ): Boolean {
            return oldItem.id == newItem.id;
        }

        override fun areContentsTheSame(
            oldItem: Crew,
            newItem: Crew
        ): Boolean {
            return oldItem == newItem
        }
    }
}