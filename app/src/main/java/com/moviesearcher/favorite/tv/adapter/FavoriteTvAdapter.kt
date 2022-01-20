package com.moviesearcher.favorite.tv.adapter

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
import com.moviesearcher.databinding.ExtendedCardViewBinding
import com.moviesearcher.favorite.FavoritesFragmentDirections
import com.moviesearcher.favorite.common.model.MarkAsFavoriteRequest
import com.moviesearcher.favorite.tv.model.FavoriteTvResponse
import com.moviesearcher.favorite.tv.model.ResultFavoriteTv
import com.moviesearcher.common.utils.Constants

class FavoriteTvAdapter(
    private val favoriteTvItems: FavoriteTvResponse,
    private val navController: NavController,
    private val sessionId: String,
    private val accountId: Long,
) : RecyclerView.Adapter<FavoriteTvAdapter.FavoriteTvViewHolder>() {
    private lateinit var cardView: MaterialCardView
    private lateinit var imageButtonRemoveFromFavorite: ImageButton

    inner class FavoriteTvViewHolder(val binding: ExtendedCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate
        private val poster = binding.posterImageView
        private val overview = binding.textViewDescription

        fun bind(tvItem: ResultFavoriteTv) {
            val currentItemPosition = favoriteTvItems.results?.indexOf(tvItem)!!

            title.text = tvItem.name
            releaseDate.text = tvItem.firstAirDate?.replace("-", ".")

            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + tvItem.posterPath)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .override(400, 600)
                .into(poster)

            overview.text = tvItem.overview
            rating.text = tvItem.getAverage()
            cardView.id = tvItem.id?.toInt()!!

            imageButtonRemoveFromFavorite.setOnClickListener {
                val removeFromFavoriteResponse =
                    Api.markAsFavorite(
                        accountId,
                        sessionId,
                        MarkAsFavoriteRequest(
                            false,
                            tvItem.id.toLong(),
                            "tv"
                        ),
                    )

                removeFromFavoriteResponse.observe(binding.root.findViewTreeLifecycleOwner()!!, {
                    if (it.success) {
                        favoriteTvItems.results.remove(tvItem)
                        notifyItemRemoved(currentItemPosition)

                        val favoriteItemRemovedSnackbar = Snackbar.make(
                            binding.root.rootView,
                            "\"${tvItem.name}\" was removed from Favorites",
                            BaseTransientBottomBar.LENGTH_LONG
                        )

                        favoriteItemRemovedSnackbar.setAction("UNDO") { view ->
                            val addToFavoriteResponse =
                                Api.markAsFavorite(
                                    accountId,
                                    sessionId,
                                    MarkAsFavoriteRequest(
                                        true,
                                        tvItem.id.toLong(),
                                        "tv"
                                    ),
                                )

                            addToFavoriteResponse.observe(
                                view.findViewTreeLifecycleOwner()!!,
                                { addToFavorite ->
                                    if (addToFavorite.success) {
                                        favoriteTvItems.results.add(currentItemPosition, tvItem)
                                        notifyItemInserted(currentItemPosition)
                                        Toast.makeText(
                                            itemView.context,
                                            "\"${tvItem.name}\" added back",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            itemView.context,
                                            "Error while adding tv back",
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
                    FavoritesFragmentDirections.actionFragmentFavoritesToTvInfoFragment(tvItem.id)
                )
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<ResultFavoriteTv>() {
        override fun areItemsTheSame(
            oldItem: ResultFavoriteTv,
            newItem: ResultFavoriteTv
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ResultFavoriteTv,
            newItem: ResultFavoriteTv
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteTvViewHolder {
        val binding = ExtendedCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        cardView = binding.cardView
        imageButtonRemoveFromFavorite = binding.imageButtonRemove

        return FavoriteTvViewHolder(binding)
    }

    override fun getItemCount(): Int = favoriteTvItems.results?.size!!
    override fun onBindViewHolder(holder: FavoriteTvViewHolder, position: Int) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }
}