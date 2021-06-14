package com.moviesearcher.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.R
import com.moviesearcher.SearchResultFragmentDirections
import com.moviesearcher.api.entity.search.Result
import com.moviesearcher.api.entity.search.SearchResponse
import com.moviesearcher.utils.Constants
import com.squareup.picasso.Picasso

class SearchAdapter(
    private val searchItems: SearchResponse,
    private val navController: NavController
) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val searchItemPoster: ImageView =
            view.findViewById(R.id.search_item_fragment_imageview)
        private val searchItemTitle: TextView = view.findViewById(R.id.search_item_fragment_title)

        fun bind(searchResultItem: Result) {
            Picasso.get()
                .load(Constants.IMAGE_URL + searchResultItem.posterPath)
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
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_search_item, parent, false)
        val searchItemPoster: ImageView = view.findViewById(R.id.search_item_fragment_imageview)

        searchItemPoster.setOnClickListener {
            val movieId = it.id

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

        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int = searchItems.results?.size!!
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val searchItem = searchItems.results?.get(position)
        holder.bind(searchItem!!)
    }
}