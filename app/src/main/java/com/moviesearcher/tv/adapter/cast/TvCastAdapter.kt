package com.moviesearcher.tv.adapter.cast

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moviesearcher.R
import com.moviesearcher.databinding.MovieCastItemBinding
import com.moviesearcher.tv.TvInfoFragmentDirections
import com.moviesearcher.tv.model.cast.Cast
import com.moviesearcher.tv.model.cast.TvCastResponse
import com.moviesearcher.utils.Constants

class TvCastAdapter(
    private val castItems: TvCastResponse,
    private val navController: NavController
) : RecyclerView.Adapter<TvCastAdapter.TvCastHolder>() {
    private lateinit var binding: MovieCastItemBinding

    inner class TvCastHolder(binding: MovieCastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val name = binding.textViewName
        private val characterName = binding.textViewCharacterName
        private val poster: ImageView = binding.posterImageView
        private val roles = mutableListOf<String>()
        private val castCardView: CardView = binding.castCardView

        fun bind(castItem: Cast) {
            castItem.roles?.forEach { roles.add(it.character!!) }

            name.text = castItem.original_name
            characterName.text = roles.joinToString()

            Glide.with(this.itemView.context)
                .load(Constants.IMAGE_URL + castItem.profile_path)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop()
                .override(400, 600)
                .into(poster)

            castCardView.setOnClickListener {
                navController.navigate(
                    TvInfoFragmentDirections.actionTvInfoFragmentToPersonInfoFragment(
                        castItem.id?.toLong()!!
                    )
                )
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TvCastAdapter.TvCastHolder {
        binding = MovieCastItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TvCastHolder(binding)
    }

    override fun getItemCount(): Int = castItems.cast?.size!!
    override fun onBindViewHolder(holder: TvCastHolder, position: Int) {
        val reply = differ.currentList[position]
        holder.bind(reply)
    }
}