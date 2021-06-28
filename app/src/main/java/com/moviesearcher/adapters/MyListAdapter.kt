package com.moviesearcher.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.moviesearcher.MyListsFragmentDirections
import com.moviesearcher.R
import com.moviesearcher.api.entity.list.ListResponse
import com.moviesearcher.api.entity.list.Result
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

        fun bind(myListResultItem: Result) {
            Picasso.get()
                .load(Constants.IMAGE_URL + myListResultItem.posterPath)
                .into(myListItemPoster)

            cardView.id = myListResultItem.id?.toInt()!!
            myListItemName.text = myListResultItem.name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_my_list_item, parent, false)
        val cardView: MaterialCardView =
            view.findViewById(R.id.material_card_view_my_list_item)

        cardView.setOnClickListener {
            val listId = it.id

            navController.navigate(
                MyListsFragmentDirections.actionMyListsFragmentToMyListFragment(
                    listId
                )
            )
        }

        return MyListViewHolder(view)
    }

    override fun getItemCount(): Int = listItems.results?.size!!
    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        val listItem = listItems.results?.get(position)
        holder.bind(listItem!!)
    }
}