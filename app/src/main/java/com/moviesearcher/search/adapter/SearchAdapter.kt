package com.moviesearcher.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.extensions.loadImage
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.ExtendedCardViewBinding
import com.moviesearcher.search.SearchResultFragmentDirections
import com.moviesearcher.search.model.Result
import com.moviesearcher.search.model.SearchResponse

class SearchAdapter(
    private val searchItems: SearchResponse,
    private val navController: NavController
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(binding: ExtendedCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val title: TextView = binding.textViewTitle
        private val rating = binding.textViewRating
        private val releaseDate = binding.textViewReleaseDate
        private val overview = binding.textViewDescription
        private val poster = binding.posterImageView
        private val cardView = binding.cardView
        private val removeButton = binding.imageButtonRemove

        fun bind(searchResultItem: Result) {
            removeButton.visibility = View.GONE
            poster.loadImage(Constants.IMAGE_URL + searchResultItem.posterPath, isCardView = true)

            if (searchResultItem.title == null) {
                title.text = searchResultItem.name
            } else {
                title.text = searchResultItem.title
            }

            if (searchResultItem.releaseDate != null) {
                releaseDate.text = searchResultItem.releaseDate.replace("-", ".")
            } else if (searchResultItem.firstAirDate != null) {
                releaseDate.text = searchResultItem.firstAirDate.replace("-", ".")
            }

            rating.text = searchResultItem.voteAverage.toString()
            overview.text = searchResultItem.overview

            cardView.setOnClickListener {
                //Only 'Movie' has a 'title', 'Tv series' has a 'name'
                if (searchResultItem.mediaType == "movie") {
                    navController.navigate(
                        SearchResultFragmentDirections.actionSearchResultFragmentToMovieInfoFragment(
                            searchResultItem.id?.toLong()!!
                        )
                    )
                } else {
                    navController.navigate(
                        SearchResultFragmentDirections.actionSearchResultFragmentToTvInfoFragment(
                            searchResultItem.id?.toLong()!!
                        )
                    )
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(
            oldItem: Result,
            newItem: Result
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Result,
            newItem: Result
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        val binding = ExtendedCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int = searchItems.results?.size!!
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }
}