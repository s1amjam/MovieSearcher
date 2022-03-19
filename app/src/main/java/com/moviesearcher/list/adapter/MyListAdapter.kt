package com.moviesearcher.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.R
import com.moviesearcher.common.model.common.MediaId
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.ExtendedCardViewBinding
import com.moviesearcher.list.ListViewModel
import com.moviesearcher.list.MyListFragmentDirections
import com.moviesearcher.list.model.Item
import com.moviesearcher.list.model.ListResponse

class MyListAdapter(
    private val listItems: ListResponse,
    private val navController: NavController,
    private val listId: Int,
    private val sessionId: String,
    private val listViewModel: ListViewModel
) : RecyclerView.Adapter<MyListAdapter.MyListViewHolder>() {

    inner class MyListViewHolder(val binding: ExtendedCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val rating = binding.textViewRating
        private val title = binding.textViewTitle
        private val releaseDate = binding.textViewReleaseDate
        private val posterImageView = binding.posterImageView
        private val overview = binding.textViewDescription
        private val cardView = binding.cardView
        private val imageButtonRemoveFromList = binding.imageButtonRemove

        private val addToListDrawable = R.drawable.ic_baseline_list_add_36
        private val addedToListDrawable = R.drawable.ic_baseline_list_added_36

        fun bind(movieItem: Item) {
            imageButtonRemoveFromList.setImageResource(addedToListDrawable)
            imageButtonRemoveFromList.tag = "false"

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

            overview.text = movieItem.overview
            rating.text = movieItem.voteAverage.toString()

            cardView.tag = movieItem.title

            imageButtonRemoveFromList.setOnClickListener {
                if (imageButtonRemoveFromList.tag == "false") {
                    listViewModel.removeFromList(
                        listId,
                        sessionId,
                        MediaId(movieItem.id!!.toLong())
                    )
                    imageButtonRemoveFromList.setImageResource(addToListDrawable)
                    imageButtonRemoveFromList.tag = "true"
                } else {
                    listViewModel.addToList(
                        listId,
                        sessionId,
                        MediaId(movieItem.id!!.toLong()),
                    )
                    imageButtonRemoveFromList.setImageResource(addedToListDrawable)
                    imageButtonRemoveFromList.tag = "false"
                }
            }

            cardView.setOnClickListener {
                //Only 'Movie' has a 'title', 'Tv series' has a 'name', so binding title to tag
                if (it.tag != null) {
                    navController.navigate(
                        MyListFragmentDirections
                            .actionFragmentMyListToMovieInfoFragment(movieItem.id!!)
                    )
                } else {
                    navController.navigate(
                        MyListFragmentDirections
                            .actionFragmentMyListToTvInfoFragment(movieItem.id!!)
                    )
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(
            oldItem: Item,
            newItem: Item
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Item,
            newItem: Item
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyListAdapter.MyListViewHolder {
        val binding = ExtendedCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MyListViewHolder(binding)
    }

    override fun getItemCount(): Int = listItems.items?.size!!
    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }
}