package com.moviesearcher.movie.adapter.recommendations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.extensions.loadImage
import com.moviesearcher.common.extensions.toOneScale
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.MovieCardViewBinding
import com.moviesearcher.favorite.movie.model.FavoriteMovieResponse
import com.moviesearcher.favorite.movie.model.ResultFavoriteMovie
import com.moviesearcher.movie.MovieInfoFragmentDirections

class RecommendationsAdapter(
    private val recommendationsItems: FavoriteMovieResponse,
    private val navController: NavController,
) : RecyclerView.Adapter<RecommendationsAdapter.RecommendationsHolder>() {
    private lateinit var binding: MovieCardViewBinding

    inner class RecommendationsHolder(binding: MovieCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate
        private val cardView = binding.trendingCardView
        private val posterImageView = binding.posterImageView

        fun bind(recommendationsItem: ResultFavoriteMovie) {
            posterImageView.loadImage(
                Constants.IMAGE_URL + recommendationsItem.posterPath,
                isCardView = true
            )
            title.text = recommendationsItem.title
            releaseDate.text = recommendationsItem.releaseDate?.replace("-", ".")
            rating.text = recommendationsItem.voteAverage?.toOneScale()

            cardView.setOnClickListener {
                navController.navigate(
                    MovieInfoFragmentDirections.actionMovieInfoFragmentSelf(recommendationsItem.id!!)
                )
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendationsAdapter.RecommendationsHolder {
        binding = MovieCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return RecommendationsHolder(binding)
    }

    private val differCallback = object : DiffUtil.ItemCallback<ResultFavoriteMovie>() {
        override fun areItemsTheSame(
            oldItem: ResultFavoriteMovie,
            newItem: ResultFavoriteMovie
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResultFavoriteMovie,
            newItem: ResultFavoriteMovie
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int = recommendationsItems.results?.size!!
    override fun onBindViewHolder(holder: RecommendationsHolder, position: Int) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }
}