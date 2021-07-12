package com.moviesearcher.adapters

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
import com.moviesearcher.MyListFragmentDirections
import com.moviesearcher.R
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.common.MediaId
import com.moviesearcher.api.entity.list.Item
import com.moviesearcher.api.entity.list.ListResponse
import com.moviesearcher.utils.Constants
import com.squareup.picasso.Picasso

class MyListAdapter(
    private val listItems: ListResponse,
    private val navController: NavController,
    private val listId: Int,
    private val sessionId: String
) : RecyclerView.Adapter<MyListAdapter.MyListViewHolder>() {
    private var adapterPos: Int = -1
    private var itemPos: Int = -1

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
        val imageButtonRemoveFromList: ImageButton =
            view.findViewById(R.id.image_button_remove_from_list)

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

        imageButtonRemoveFromList.setOnClickListener {
            val removeFromListResponse =
                Api.removeFromList(listId, sessionId, MediaId(cardView.id.toLong()))

            removeFromListResponse.observe(view.findViewTreeLifecycleOwner()!!, {
                if (it.statusMessage == "The item/record was deleted successfully.") {
                    listItems.items!!.removeAt(this.itemPos)
                    this.notifyItemRemoved(adapterPos)
                }
            })
        }

        return MyListViewHolder(view)
    }

    override fun getItemCount(): Int = listItems.items?.size!!
    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        adapterPos = holder.adapterPosition
        itemPos = position

        val listItem = listItems.items?.get(position)
        holder.bind(listItem!!)
    }
}