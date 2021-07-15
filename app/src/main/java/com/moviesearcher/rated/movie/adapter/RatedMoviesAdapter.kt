package com.moviesearcher.rated.movie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.moviesearcher.R
import com.moviesearcher.rated.RatedFragmentDirections
import com.moviesearcher.rated.movie.model.RatedMoviesResponse
import com.moviesearcher.rated.movie.model.RatedMoviesResult
import com.moviesearcher.utils.Constants
import com.squareup.picasso.Picasso

class RatedMoviesAdapter(
    private val ratedMovieItems: RatedMoviesResponse,
    private val navController: NavController
) : RecyclerView.Adapter<RatedMoviesAdapter.RatedMovieViewHolder>() {

    class RatedMovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ratedMovieItemPoster: ImageView =
            view.findViewById(R.id.image_view_rated_movie_item)
        private val ratedMovieItemName: TextView =
            view.findViewById(R.id.text_view_rated_movie_item_name)
        private val cardView: MaterialCardView =
            view.findViewById(R.id.material_card_view_rated_movie_item)

        fun bind(ratedMovieResultItem: RatedMoviesResult) {
            Picasso.get()
                .load(Constants.IMAGE_URL + ratedMovieResultItem.posterPath)
                .into(ratedMovieItemPoster)

            cardView.id = ratedMovieResultItem.id?.toInt()!!
            ratedMovieItemName.text = ratedMovieResultItem.title
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RatedMovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_rated_movie_item, parent, false)
        val cardView: MaterialCardView =
            view.findViewById(R.id.material_card_view_rated_movie_item)

        cardView.setOnClickListener {
            val movieId = it.id.toLong()

            navController.navigate(
                RatedFragmentDirections.actionRatedFragmentToMovieInfoFragment(
                    movieId
                )
            )
        }

        return RatedMovieViewHolder(view)
    }

    override fun getItemCount(): Int = ratedMovieItems.results?.size!!
    override fun onBindViewHolder(holder: RatedMovieViewHolder, position: Int) {
        val ratedMovieItem = ratedMovieItems.results?.get(position)
        holder.bind(ratedMovieItem!!)
    }
}