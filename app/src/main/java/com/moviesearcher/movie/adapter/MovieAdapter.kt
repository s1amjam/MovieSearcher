package com.moviesearcher.movie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.moviesearcher.MovieSearcherFragmentDirections
import com.moviesearcher.databinding.PosterImageViewBinding
import com.moviesearcher.movie.model.Result
import com.moviesearcher.movie.model.TrendingResponse
import com.moviesearcher.utils.Constants

class MovieAdapter(
    private val movieItems: TrendingResponse,
    private val navController: NavController
) : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {
    private lateinit var posterImageView: ImageView
    private lateinit var binding: PosterImageViewBinding

    inner class MovieHolder(binding: PosterImageViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieItem: Result) {
            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + movieItem.posterPath)
                .override(SIZE_ORIGINAL, SIZE_ORIGINAL)
                .into(posterImageView)
            posterImageView.id = movieItem.id!!
            posterImageView.tag = movieItem.title
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieHolder {
        binding = PosterImageViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MovieHolder(binding)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = movieItems.results?.size!!
    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        posterImageView = binding.posterImageView

        posterImageView.setOnClickListener {
            val movieId = it.id.toLong()

            //Only 'Movie' has a 'title', 'Tv series' has a 'name', so binding title to tag
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

private val ITEM_COMPARATOR = object :
    DiffUtil.ItemCallback<TrendingResponse>() {
    override fun areItemsTheSame(
        oldItem: TrendingResponse, newItem:
        TrendingResponse
    ): Boolean {
        return oldItem.results == newItem.results
    }

    override fun areContentsTheSame(
        oldItem: TrendingResponse, newItem:
        TrendingResponse
    ): Boolean {
        return oldItem == newItem
    }
}