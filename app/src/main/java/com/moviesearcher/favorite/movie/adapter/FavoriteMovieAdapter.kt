package com.moviesearcher.favorite.movie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.R
import com.moviesearcher.common.extensions.loadImage
import com.moviesearcher.common.extensions.toOneScale
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.ExtendedCardViewBinding
import com.moviesearcher.favorite.FavoritesFragmentDirections
import com.moviesearcher.favorite.movie.model.FavoriteMovieResponse
import com.moviesearcher.favorite.movie.model.ResultFavoriteMovie

class FavoriteMovieAdapter(
    private val favoriteMovieItems: FavoriteMovieResponse,
    private val navController: NavController,
    private val itemClickListener: ItemClickListener,
    private val isTv: Boolean = false,
) : RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder>() {

    inner class FavoriteMovieViewHolder(val binding: ExtendedCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate
        private val poster = binding.posterImageView
        private val overview = binding.textViewDescription
        private val imageButtonRemoveFromFavorite = binding.imageButtonRemove
        private val cardView = binding.cardView

        fun bind(movieItem: ResultFavoriteMovie) {
            val mediaInfo: MutableMap<String, Long> = mutableMapOf()

            poster.loadImage(Constants.IMAGE_URL + movieItem.posterPath, isCardView = true)
            title.text = movieItem.title
            releaseDate.text = movieItem.releaseDate?.replace("-", ".")
            imageButtonRemoveFromFavorite.setImageResource(R.drawable.ic_round_star_filled_36)
            overview.text = movieItem.overview
            rating.text = movieItem.voteAverage?.toOneScale()
            cardView.id = movieItem.id?.toInt()!!
            imageButtonRemoveFromFavorite.tag = "false"

            if (isTv) {
                mediaInfo["tv"] = cardView.id.toLong()

                cardView.setOnClickListener {
                    navController.navigate(
                        FavoritesFragmentDirections
                            .actionFragmentFavoritesToTvInfoFragment(movieItem.id)
                    )
                }
            } else {
                mediaInfo["movie"] = cardView.id.toLong()

                cardView.setOnClickListener {
                    navController.navigate(
                        FavoritesFragmentDirections
                            .actionFragmentFavoritesToMovieInfoFragment(movieItem.id)
                    )
                }
            }

            imageButtonRemoveFromFavorite.setOnClickListener {
                itemClickListener.onItemClick(
                    imageButtonRemoveFromFavorite,
                    mediaInfo,
                    binding.root.context,
                    binding.root.findViewTreeLifecycleOwner()!!
                )
            }
        }
    }

    interface ItemClickListener {
        fun onItemClick(
            button: ImageButton,
            media: MutableMap<String, Long> = mutableMapOf(),
            context: Context,
            lifecycleOwner: LifecycleOwner
        )
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteMovieViewHolder {
        val binding = ExtendedCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return FavoriteMovieViewHolder(binding)
    }

    override fun getItemCount(): Int = favoriteMovieItems.results?.size!!
    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}