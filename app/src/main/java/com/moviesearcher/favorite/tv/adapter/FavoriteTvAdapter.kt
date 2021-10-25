package com.moviesearcher.favorite.tv.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.moviesearcher.api.Api
import com.moviesearcher.databinding.FragmentFavoriteTvItemBinding
import com.moviesearcher.favorite.FavoritesFragmentDirections
import com.moviesearcher.favorite.common.model.MarkAsFavoriteRequest
import com.moviesearcher.favorite.tv.model.FavoriteTvResponse
import com.moviesearcher.favorite.tv.model.ResultFavoriteTv
import com.moviesearcher.utils.Constants
import com.moviesearcher.utils.Constants.DURATION_5_SECONDS

class FavoriteTvAdapter(
    private val favoriteTvItems: FavoriteTvResponse,
    private val navController: NavController,
    private val sessionId: String,
    private val accountId: Long,
) : RecyclerView.Adapter<FavoriteTvAdapter.FavoriteTvViewHolder>() {
    private lateinit var cardView: MaterialCardView
    private lateinit var imageButtonRemoveFromFavorite: ImageButton

    inner class FavoriteTvViewHolder(val binding: FragmentFavoriteTvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate
        private val poster = binding.posterImageView
        private val overview = binding.textViewDescription

        fun bind(tvItem: ResultFavoriteTv) {
            title.text = tvItem.name
            releaseDate.text = tvItem.firstAirDate?.replace("-", ".")

            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + tvItem.posterPath)
                .centerCrop()
                .override(400, 600)
                .into(poster)

            overview.text = tvItem.overview
            rating.text = tvItem.voteAverage.toString()
            cardView.id = tvItem.id?.toInt()!!
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteTvViewHolder {
        val binding = FragmentFavoriteTvItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        cardView = binding.favoriteCardView
        imageButtonRemoveFromFavorite = binding.imageButtonRemoveFromFavorite

        return FavoriteTvViewHolder(binding)
    }

    override fun getItemCount(): Int = favoriteTvItems.results?.size!!

    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: FavoriteTvViewHolder, position: Int) {
        imageButtonRemoveFromFavorite.setOnClickListener {
            val tvToRemove = favoriteTvItems.results?.get(position)

            val removeFromFavoriteResponse =
                Api.markAsFavorite(
                    accountId,
                    sessionId,
                    MarkAsFavoriteRequest(
                        false,
                        tvToRemove?.id!!.toLong(),
                        "tv"
                    ),
                )

            removeFromFavoriteResponse.observe(holder.binding.root.findViewTreeLifecycleOwner()!!, {
                if (it.success) {
                    favoriteTvItems.results?.removeAt(position)
                    notifyItemRemoved(position)

                    val favoriteItemRemovedSnackbar = Snackbar.make(
                        holder.binding.root.rootView,
                        "\"${tvToRemove.name}\" was removed from Favorites",
                        DURATION_5_SECONDS
                    )

                    favoriteItemRemovedSnackbar.setAction("UNDO") { view ->
                        val addToFavoriteResponse =
                            Api.markAsFavorite(
                                accountId,
                                sessionId,
                                MarkAsFavoriteRequest(
                                    true,
                                    tvToRemove.id.toLong(),
                                    "tv"
                                ),
                            )

                        addToFavoriteResponse.observe(
                            view.findViewTreeLifecycleOwner()!!,
                            { addToFavorite ->
                                if (addToFavorite.success) {
                                    favoriteTvItems.results?.add(position, tvToRemove)
                                    notifyItemInserted(position)
                                    Toast.makeText(
                                        holder.itemView.context,
                                        "\"${tvToRemove.name}\" added back",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        holder.itemView.context,
                                        "Error while adding tv back",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                    }
                    favoriteItemRemovedSnackbar.show()
                } else {
                    Toast.makeText(
                        holder.itemView.context,
                        "Error while removing from favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        cardView.setOnClickListener {
            val tvId = it.id.toLong()

            navController.navigate(
                FavoritesFragmentDirections.actionFragmentFavoritesToTvInfoFragment(tvId)
            )
        }

        val listItem = favoriteTvItems.results?.get(position)
        holder.bind(listItem!!)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}