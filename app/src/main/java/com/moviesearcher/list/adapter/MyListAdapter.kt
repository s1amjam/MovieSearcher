package com.moviesearcher.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.moviesearcher.R
import com.moviesearcher.api.Api
import com.moviesearcher.common.model.common.MediaId
import com.moviesearcher.list.MyListFragmentDirections
import com.moviesearcher.list.model.Item
import com.moviesearcher.list.model.ListResponse
import com.moviesearcher.utils.Constants
import com.squareup.picasso.Picasso

class MyListAdapter(
    private val listItems: ListResponse,
    private val navController: NavController,
    private val listId: Int,
    private val sessionId: String
) : RecyclerView.Adapter<MyListAdapter.MyListViewHolder>() {

    class MyListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val myListItemPoster: ImageView = view.findViewById(R.id.image_view_my_list_item)
        private val myListItemName: TextView = view.findViewById(R.id.text_view_my_list_item_name)
        private val cardView: MaterialCardView =
            view.findViewById(R.id.material_card_view_my_list_item)

        fun bind(myListResultItem: Item) {
            Picasso.get()
                .load(Constants.IMAGE_URL + myListResultItem.posterPath)
                .into(myListItemPoster)

            cardView.tag = myListResultItem.mediaType
            cardView.id = myListResultItem.id!!.toInt()
            myListItemName.text = myListResultItem.title
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_my_list_item, parent, false)
        val cardView: MaterialCardView = view.findViewById(R.id.material_card_view_my_list_item)

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

        return MyListViewHolder(view)
    }

    override fun getItemCount(): Int = listItems.items?.size!!
    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        val cardView: MaterialCardView =
            holder.itemView.findViewById(R.id.material_card_view_my_list_item)
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
}