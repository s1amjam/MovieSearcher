package com.moviesearcher.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.moviesearcher.api.Api
import com.moviesearcher.common.model.common.MediaId
import com.moviesearcher.databinding.FragmentMyListItemBinding
import com.moviesearcher.list.MyListFragmentDirections
import com.moviesearcher.list.model.Item
import com.moviesearcher.list.model.ListResponse
import com.moviesearcher.utils.Constants

class MyListAdapter(
    private val listItems: ListResponse,
    private val navController: NavController,
    private val listId: Int,
    private val sessionId: String
) : RecyclerView.Adapter<MyListAdapter.MyListViewHolder>() {
    private lateinit var cardView: MaterialCardView
    private lateinit var imageButtonRemoveFromList: ImageButton

    inner class MyListViewHolder(val binding: FragmentMyListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate
        private val posterImageView = binding.posterImageView
        private val overview = binding.textViewDescription

        fun bind(movieItem: Item) {
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
                .centerCrop()
                .override(400, 600)
                .into(posterImageView)

            overview.text = movieItem.overview
            rating.text = movieItem.voteAverage.toString()

            cardView.tag = movieItem.title
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyListViewHolder {
        val binding = FragmentMyListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        cardView = binding.listItemCardView
        imageButtonRemoveFromList = binding.imageButtonRemoveFromList

        return MyListViewHolder(binding)
    }

    override fun getItemCount(): Int = listItems.items?.size!!
    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        imageButtonRemoveFromList.setOnClickListener {
            val removeFromListResponse =
                Api.removeFromList(
                    listId,
                    sessionId,
                    MediaId(listItems.items?.get(position)?.id!!.toLong())
                )

            removeFromListResponse.observe(holder.itemView.findViewTreeLifecycleOwner()!!, {
                if (it.success) {
                    listItems.items.removeAt(position)
                    notifyItemRemoved(position)
                } else {
                    Toast.makeText(
                        holder.itemView.context,
                        "Error while removing from list",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        cardView.setOnClickListener {
            val movieId = it.id.toLong()

            //Only 'Movie' has a 'title', 'Tv series' has a 'name', so binding title to tag
            if (it.tag != null) {
                navController.navigate(
                    MyListFragmentDirections
                        .actionFragmentMyListToMovieInfoFragment(movieId)
                )
            } else {
                navController.navigate(
                    MyListFragmentDirections
                        .actionFragmentMyListToTvInfoFragment(movieId)
                )
            }
        }

        val listItem = listItems.items?.get(position)
        holder.bind(listItem!!)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}