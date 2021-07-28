package com.moviesearcher.movie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.MovieSearcherFragmentDirections
import com.moviesearcher.R
import com.moviesearcher.movie.model.Result
import com.moviesearcher.movie.model.TrendingResponse
import com.moviesearcher.utils.Constants
import com.squareup.picasso.Picasso

class MovieAdapter(
    private val movieItems: TrendingResponse,
    private val navController: NavController
) : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {

    private lateinit var posterImageView: ImageView

    class MovieHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val moviePoster: ImageView = view.findViewById(R.id.poster_image_view)

        fun bind(movieItem: Result) {
            Picasso.get()
                .load(Constants.IMAGE_URL + movieItem.posterPath)
                .into(moviePoster)
            moviePoster.id = movieItem.id!!
            moviePoster.tag = movieItem.title
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.poster_image_view, parent, false)

        posterImageView = view.findViewById(R.id.poster_image_view)

        posterImageView.setOnClickListener {
            val movieId = it.id.toLong()

            //Only 'Movie' has a 'title', 'Tv series' has a 'name'
            if (it.tag != null) {
                navController.navigate(
                    MovieSearcherFragmentDirections
                        .actionMovieSearcherFragmentToMovieInfoFragment(movieId)
                )
            } else {
                navController.navigate(
                    MovieSearcherFragmentDirections
                        .actionMovieSearcherFragmentToTvInfoFragment(movieId)
                )
            }
        }

        return MovieHolder(view)
    }

    override fun getItemCount(): Int = movieItems.results?.size!!
    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val movieItem = movieItems.results?.get(position)
        holder.bind(movieItem!!)
    }

    class GridSpacingItemDecoration(
        private val spanCount: Int,
        private val spacing: Int,
        private val includeEdge: Boolean
    ) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: android.graphics.Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view)
            val column = position % spanCount
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column + 1) * spacing / spanCount
                if (position < spanCount) {
                    outRect.top = spacing
                }
                outRect.bottom = spacing
            } else {
                outRect.left = column * spacing / spanCount
                outRect.right = spacing - (column + 1) * spacing / spanCount
                if (position >= spanCount) {
                    outRect.top = spacing
                }
            }
        }
    }
}