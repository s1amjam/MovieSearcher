package com.moviesearcher.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.moviesearcher.databinding.FragmentSearchItemBinding
import com.moviesearcher.search.SearchResultFragmentDirections
import com.moviesearcher.search.model.Result
import com.moviesearcher.search.model.SearchResponse
import com.moviesearcher.utils.Constants

class SearchAdapter(
    private val searchItems: SearchResponse,
    private val navController: NavController
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    lateinit var searchItemPoster: ImageView

    inner class SearchViewHolder(binding: FragmentSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val searchItemTitle: TextView = binding.searchItemFragmentTitle

        fun bind(searchResultItem: Result) {
            Glide.with(this.itemView)
                .load(Constants.IMAGE_URL + searchResultItem.posterPath)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(searchItemPoster)
            if (searchResultItem.title == null) {
                searchItemTitle.text = searchResultItem.name
                searchItemPoster.tag = "tv"
                searchItemPoster.id = searchResultItem.id!!
            } else {
                searchItemTitle.text = searchResultItem.title
                searchItemPoster.tag = "movie"
                searchItemPoster.id = searchResultItem.id!!
            }
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
        searchItemPoster = binding.searchItemFragmentImageview

        searchItemPoster.setOnClickListener {
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
}