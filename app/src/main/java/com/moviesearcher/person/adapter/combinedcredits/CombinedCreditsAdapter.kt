package com.moviesearcher.person.adapter.combinedcredits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.extensions.loadImage
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.MovieCardViewBinding
import com.moviesearcher.person.PersonInfoFragmentDirections
import com.moviesearcher.person.model.combinedcredits.Cast

class CombinedCreditsAdapter(private val navController: NavController) :
    ListAdapter<Cast, CombinedCreditsAdapter.CombinedCreditsHolder>(
        AsyncDifferConfig.Builder(DiffCallback()).build()
    ) {

    inner class CombinedCreditsHolder(private val binding: MovieCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.trendingCardView.setOnClickListener {
                val movieId = it.id.toLong()

                //Only 'Movie' has a 'title', 'Tv series' has a 'name', so binding title to tag
                if (it.tag != null) {
                    navController.navigate(
                        PersonInfoFragmentDirections
                            .actionPersonInfoFragmentToMovieInfoFragment(movieId)
                    )
                } else {
                    navController.navigate(
                        PersonInfoFragmentDirections
                            .actionPersonInfoFragmentToTvInfoFragment(movieId)
                    )
                }
            }
        }

        fun bind(movieItem: Cast) {
            if (movieItem.title != null) {
                binding.textViewTitle.text = movieItem.title
            } else if (movieItem.name != null) {
                binding.textViewTitle.text = movieItem.name
            }

            if (movieItem.releaseDate != null) {
                binding.textViewReleaseDate.text = movieItem.releaseDate.replace("-", ".")
            } else if (movieItem.firstAirDate != null) {
                binding.textViewReleaseDate.text = movieItem.firstAirDate.replace("-", ".")
            }

            binding.posterImageView.loadImage(
                Constants.IMAGE_URL + movieItem.posterPath,
                isCardView = true
            )
            binding.trendingCardView.id = movieItem.id!!
            binding.trendingCardView.tag = movieItem.title
            binding.textViewRating.text = movieItem.voteAverage.toString()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CombinedCreditsHolder {
        return CombinedCreditsHolder(
            MovieCardViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CombinedCreditsHolder, position: Int) {
        holder.bind(currentList[position])
    }

    private class DiffCallback : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.id == newItem.id;
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }
    }
}