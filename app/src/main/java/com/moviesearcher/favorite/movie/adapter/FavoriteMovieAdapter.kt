package com.moviesearcher.favorite.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.moviesearcher.R
import com.moviesearcher.api.Api
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.ExtendedCardViewBinding
import com.moviesearcher.favorite.FavoritesFragmentDirections
import com.moviesearcher.favorite.model.MarkAsFavoriteRequest
import com.moviesearcher.favorite.movie.model.FavoriteMovieResponse
import com.moviesearcher.favorite.movie.model.ResultFavoriteMovie

class FavoriteMovieAdapter(
    private val favoriteMovieItems: FavoriteMovieResponse,
    private val navController: NavController,
    private val sessionId: String,
    private val accountId: Long,
) : RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder>() {
    private lateinit var cardView: MaterialCardView
    private lateinit var imageButtonRemoveFromFavorite: ImageButton

    inner class FavoriteMovieViewHolder(val binding: ExtendedCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate
        private val poster = binding.posterImageView
        private val overview = binding.textViewDescription

        fun bind(movieItem: ResultFavoriteMovie) {
            val currentItemPosition = favoriteMovieItems.results?.indexOf(movieItem)!!

            title.text = movieItem.title
            releaseDate.text = movieItem.releaseDate?.replace("-", ".")

            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + movieItem.posterPath)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .override(400, 600)
                .into(poster)

            overview.text = movieItem.overview
            rating.text = movieItem.getAverage()
            cardView.id = movieItem.id?.toInt()!!

            imageButtonRemoveFromFavorite.setOnClickListener {
                val removeFromFavoriteResponse =
                    Api.markAsFavorite(
                        accountId,
                        sessionId,
                        MarkAsFavoriteRequest(
                            false,
                            movieItem.id.toLong(),
                            "movie"
                        ),
                    )

                removeFromFavoriteResponse.observe(binding.root.findViewTreeLifecycleOwner()!!, {
                    if (it.success) {
                        favoriteMovieItems.results.remove(movieItem)
                        notifyItemRemoved(currentItemPosition)

                        val favoriteItemRemovedSnackbar = Snackbar.make(
                            binding.root.rootView,
                            "\"${movieItem.title}\" was removed from Favorites",
                            BaseTransientBottomBar.LENGTH_LONG
                        )

                        favoriteItemRemovedSnackbar.setAction("UNDO") { view ->
                            val addToFavoriteResponse =
                                Api.markAsFavorite(
                                    accountId,
                                    sessionId,
                                    MarkAsFavoriteRequest(
                                        true,
                                        movieItem.id.toLong(),
                                        "movie"
                                    ),
                                )

                            addToFavoriteResponse.observe(
                                view.findViewTreeLifecycleOwner()!!,
                                { addToFavorite ->
                                    if (addToFavorite.success) {
                                        favoriteMovieItems.results.add(
                                            currentItemPosition,
                                            movieItem
                                        )
                                        notifyItemInserted(currentItemPosition)
                                        Toast.makeText(
                                            itemView.context,
                                            "\"${movieItem.title}\" added back",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            itemView.context,
                                            "Error while adding movie back",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                        }
                        favoriteItemRemovedSnackbar.show()
                    } else {
                        Toast.makeText(
                            itemView.context,
                            "Error while removing from favorites",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }

            cardView.setOnClickListener {
                navController.navigate(
                    FavoritesFragmentDirections
                        .actionFragmentFavoritesToMovieInfoFragment(movieItem.id)
                )
            }
        }
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
        cardView = binding.cardView
        imageButtonRemoveFromFavorite = binding.imageButtonRemove

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