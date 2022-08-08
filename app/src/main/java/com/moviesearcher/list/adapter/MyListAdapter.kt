package com.moviesearcher.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.R
import com.moviesearcher.common.extensions.loadImage
import com.moviesearcher.common.model.common.MediaId
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.ExtendedCardViewBinding
import com.moviesearcher.list.ListViewModel
import com.moviesearcher.list.MyListFragmentDirections
import com.moviesearcher.list.model.Item

class MyListAdapter(
    private val navController: NavController,
    private val listId: Int,
    private val sessionId: String,
    private val listViewModel: ListViewModel
) : ListAdapter<Item, MyListAdapter.MyListViewHolder>(
    AsyncDifferConfig.Builder(DiffCallback()).build()
) {

    inner class MyListViewHolder(private val binding: ExtendedCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val addToListDrawable = R.drawable.ic_baseline_list_add_36
        private val addedToListDrawable = R.drawable.ic_baseline_list_added_36

        init {
            binding.imageButtonRemove.setOnClickListener {
                val id = currentList[adapterPosition].id
                if (binding.imageButtonRemove.tag == "false") {
                    id?.let { it1 -> MediaId(it1) }?.let { it2 ->
                        listViewModel.removeFromList(
                            listId,
                            sessionId,
                            it2
                        )
                    }
                    binding.imageButtonRemove.setImageResource(addToListDrawable)
                    binding.imageButtonRemove.tag = "true"
                } else {
                    id?.let { it1 -> MediaId(it1) }?.let { it2 ->
                        listViewModel.addToList(
                            listId,
                            sessionId,
                            it2,
                        )
                    }
                    binding.imageButtonRemove.setImageResource(addedToListDrawable)
                    binding.imageButtonRemove.tag = "false"
                }
            }

            binding.cardView.setOnClickListener {
                //Only 'Movie' has a 'title', 'Tv series' has a 'name', so binding title to tag
                val id = currentList[adapterPosition].id
                if (it.tag != null) {
                    id?.let { it1 ->
                        navController.navigate(
                            MyListFragmentDirections
                                .actionFragmentMyListToMovieInfoFragment(it1)
                        )
                    }
                } else {
                    id?.let { it1 ->
                        navController.navigate(
                            MyListFragmentDirections
                                .actionFragmentMyListToTvInfoFragment(it1)
                        )
                    }
                }
            }
        }

        fun bind(movieItem: Item) {
            binding.apply {
                imageButtonRemove.setImageResource(addedToListDrawable)
                imageButtonRemove.tag = "false"

                if (movieItem.title != null) {
                    textViewTitle.text = movieItem.title
                } else if (movieItem.name != null) {
                    textViewTitle.text = movieItem.name
                }

                if (movieItem.releaseDate != null) {
                    textViewReleaseDate.text = movieItem.releaseDate.replace("-", ".")
                } else if (movieItem.firstAirDate != null) {
                    textViewReleaseDate.text = movieItem.firstAirDate.replace("-", ".")
                }

                posterImageView.loadImage(
                    Constants.IMAGE_URL + movieItem.posterPath,
                    isCardView = true
                )
                textViewDescription.text = movieItem.overview
                textViewRating.text = movieItem.voteAverage.toString()
                cardView.tag = movieItem.title
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyListAdapter.MyListViewHolder {
        return MyListViewHolder(
            ExtendedCardViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    private class DiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id;
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }
}