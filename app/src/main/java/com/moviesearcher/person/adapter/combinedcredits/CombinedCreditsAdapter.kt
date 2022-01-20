package com.moviesearcher.person.adapter.combinedcredits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.R
import com.moviesearcher.databinding.MovieCardViewBinding
import com.moviesearcher.person.PersonInfoFragmentDirections
import com.moviesearcher.person.model.combinedcredits.Cast
import com.moviesearcher.person.model.combinedcredits.CombinedCreditsResponse
import com.moviesearcher.common.utils.Constants

class CombinedCreditsAdapter(
    private val creditsItems: CombinedCreditsResponse,
    private val navController: NavController,
    private val accountId: Long?,
    private val sessionId: String?,
) : RecyclerView.Adapter<CombinedCreditsAdapter.CombinedCreditsHolder>() {
    private lateinit var binding: MovieCardViewBinding

    inner class CombinedCreditsHolder(binding: MovieCardViewBinding) : RecyclerView.ViewHolder(binding.root) {
        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate
        private val imageViewWatchlist = binding.imageViewWatchlist
        private val posterImageView = binding.posterImageView
        private val cardView = binding.trendingCardView

        fun bind(movieItem: Cast) {
            if (movieItem.title != null) {
                title.text = movieItem.title
            } else if (movieItem.name != null) {
                title.text = movieItem.name
            }

            if (movieItem.releaseDate != null) {
                releaseDate.text = movieItem.releaseDate.replace("-", ".")
            } else if (movieItem.firstAirDate != null) {
                releaseDate.text = movieItem.firstAirDate.replace("-", ".")
            }

            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + movieItem.posterPath)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .override(400, 600)
                .into(posterImageView)

            cardView.id = movieItem.id!!
            cardView.tag = movieItem.title
            rating.text = movieItem.voteAverage.toString()

            cardView.setOnClickListener {
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
    }

    private val differCallback = object : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CombinedCreditsHolder {
        binding = MovieCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CombinedCreditsHolder(binding)
    }

    override fun getItemCount(): Int = creditsItems.cast?.size!!
    override fun onBindViewHolder(holder: CombinedCreditsHolder, position: Int) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }
}