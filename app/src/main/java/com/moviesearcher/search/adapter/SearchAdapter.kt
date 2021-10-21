package com.moviesearcher.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.databinding.FragmentSearchItemBinding
import com.moviesearcher.search.SearchResultFragmentDirections
import com.moviesearcher.search.model.Result
import com.moviesearcher.search.model.SearchResponse
import com.moviesearcher.utils.Constants

class SearchAdapter(
    private val searchItems: SearchResponse,
    private val navController: NavController
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    private lateinit var cardView: CardView

    inner class SearchViewHolder(binding: FragmentSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val title: TextView = binding.textViewTitle
        private val rating = binding.textViewRating
        private val releaseDate = binding.textViewReleaseDate
        private val overview = binding.textViewDescription
        private val poster = binding.posterImageView

        fun bind(searchResultItem: Result) {
            Glide.with(this.itemView)
                .load(Constants.IMAGE_URL + searchResultItem.posterPath)
                .centerCrop()
                .override(400, 600)
                .into(poster)

            if (searchResultItem.title == null) {
                title.text = searchResultItem.name
                cardView.tag = "tv"
                cardView.id = searchResultItem.id!!
            } else {
                title.text = searchResultItem.title
                cardView.tag = "movie"
                cardView.id = searchResultItem.id!!
            }

            if (searchResultItem.releaseDate != null) {
                releaseDate.text = searchResultItem.releaseDate.replace("-", ".")
            } else if (searchResultItem.firstAirDate != null) {
                releaseDate.text = searchResultItem.firstAirDate.replace("-", ".")
            }

            rating.text = searchResultItem.voteAverage.toString()
            overview.text = searchResultItem.overview
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        val binding = FragmentSearchItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        cardView = binding.searchItemCardView

        cardView.setOnClickListener {
            val movieId = it.id.toLong()

            //Only 'Movie' has a 'title', 'Tv series' has a 'name'
            if (it.tag == "movie") {
                navController.navigate(
                    SearchResultFragmentDirections.actionSearchResultFragmentToMovieInfoFragment(
                        movieId
                    )
                )
            } else {
                navController.navigate(
                    SearchResultFragmentDirections.actionSearchResultFragmentToTvInfoFragment(
                        movieId
                    )
                )
            }
        }

        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int = searchItems.results?.size!!
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val searchItem = searchItems.results?.get(position)
        holder.bind(searchItem!!)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}