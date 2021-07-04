package com.moviesearcher.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.moviesearcher.MyListFragmentDirections
import com.moviesearcher.R
import com.moviesearcher.api.entity.list.Item
import com.moviesearcher.api.entity.list.ListResponse
import com.moviesearcher.utils.Constants
import com.squareup.picasso.Picasso

class MyListAdapter(
    private val listItems: ListResponse,
    private val navController: NavController
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
            val mediaId = it.id
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
        val listItem = listItems.items?.get(position)
        holder.bind(listItem!!)
    }
}