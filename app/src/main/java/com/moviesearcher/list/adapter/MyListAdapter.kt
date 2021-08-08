package com.moviesearcher.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.android.material.card.MaterialCardView
import com.moviesearcher.R
import com.moviesearcher.api.Api
import com.moviesearcher.common.model.common.MediaId
import com.moviesearcher.databinding.FragmentMyListItemBinding
import com.moviesearcher.list.MyListFragmentDirections
import com.moviesearcher.list.model.Item
import com.moviesearcher.list.model.ListResponse
import com.moviesearcher.utils.Constants

class MyListAdapter(
    private val listItems: ListResponse,
    private val navController: NavController,
    private val listId: Int,
    private val sessionId: String
) : RecyclerView.Adapter<MyListAdapter.MyListViewHolder>() {
    lateinit var cardView: MaterialCardView

    inner class MyListViewHolder(val binding: FragmentMyListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val myListItemPoster: ImageView = binding.imageViewMyListItem
        private val myListItemName: TextView = binding.textViewMyListItemName

        fun bind(myListResultItem: Item) {
            Glide.with(this.itemView)
                .load(Constants.IMAGE_URL + myListResultItem.posterPath)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(myListItemPoster)
            cardView.tag = myListResultItem.mediaType
            cardView.id = myListResultItem.id!!.toInt()
            if (myListResultItem.title == null) {
                myListItemName.text = myListResultItem.name
            } else {
                myListItemName.text = myListResultItem.title
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyListViewHolder {
        val binding = FragmentMyListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        cardView = binding.materialCardViewMyListItem

        cardView.setOnClickListener {
            val mediaId = it.id.toLong()
            val mediaType = it.tag

            if (mediaType == "movie") {
                navController.navigate(
                    MyListFragmentDirections.actionFragmentMyListToMovieInfoFragment(
                        mediaId
                    )
                )
            } else {
                navController.navigate(
                    MyListFragmentDirections.actionFragmentMyListToTvInfoFragment(
                        mediaId
                    )
                )
            }
        }

        return MyListViewHolder(binding)
    }

    override fun getItemCount(): Int = listItems.items?.size!!
    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        val imageButtonRemoveFromList: ImageButton =
            holder.itemView.findViewById(R.id.image_button_remove_from_list)

        imageButtonRemoveFromList.setOnClickListener {
            val removeFromListResponse =
                Api.removeFromList(listId, sessionId, MediaId(cardView.id.toLong()))

            removeFromListResponse.observe(holder.itemView.findViewTreeLifecycleOwner()!!, {
                if (it.statusMessage == "The item/record was deleted successfully.") {
                    listItems.items!!.removeAt(holder.adapterPosition)
                    this.notifyItemRemoved(holder.adapterPosition)
                }
            })
        }

        val listItem = listItems.items?.get(position)
        holder.bind(listItem!!)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}