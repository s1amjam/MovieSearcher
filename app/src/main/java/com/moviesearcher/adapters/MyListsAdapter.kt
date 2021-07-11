package com.moviesearcher.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.moviesearcher.MyListsFragmentDirections
import com.moviesearcher.R
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.list.ListsResponse
import com.moviesearcher.api.entity.list.Result
import com.moviesearcher.utils.Constants
import com.squareup.picasso.Picasso

class MyListsAdapter(
    private val listItems: ListsResponse,
    private val navController: NavController,
    private val sessionId: String
) : RecyclerView.Adapter<MyListsAdapter.MyListsViewHolder>() {
    private var adapterPos: Int = -1
    private var itemPos: Int = -1
    private var listId: Int = -1

    class MyListsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val myListItemPoster: ImageView = view.findViewById(R.id.image_view_my_lists_item)
        private val myListItemName: TextView = view.findViewById(R.id.text_view_my_lists_item_name)
        private val cardView: MaterialCardView =
            view.findViewById(R.id.material_card_view_my_lists_item)
        var listId: Int = -1

        fun bind(myListResultItem: Result) {
            Picasso.get()
                .load(Constants.IMAGE_URL + myListResultItem.posterPath)
                .into(myListItemPoster)

            listId = myListResultItem.id?.toInt()!!
            myListItemName.text = myListResultItem.name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyListsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_my_lists_item, parent, false)
        val cardView: MaterialCardView =
            view.findViewById(R.id.material_card_view_my_lists_item)
        val imageButtonDeleteList: ImageButton = view.findViewById(R.id.image_button_delete_list)

        cardView.setOnClickListener {
            navController.navigate(
                MyListsFragmentDirections.actionMyListsFragmentToMyListFragment(it.id)
            )
        }

        //TODO: for now its bugged
        // https://trello.com/c/slruAstb/75-return-a-proper-response-when-lists-are-deleted
        imageButtonDeleteList.setOnClickListener {
            AlertDialog.Builder(view.context)
                .setTitle("Delete list?")
                .setPositiveButton(
                    R.string.ok
                ) { dialog, _ ->
                    val deleteListResponse = Api.deleteList(listId, sessionId)

                    deleteListResponse.observe(view.findViewTreeLifecycleOwner()!!, {
                        if (it.statusMessage == "The item/record was deleted successfully.") {
                            listItems.results!!.removeAt(this.itemPos)
                            this.notifyItemRemoved(adapterPos)
                            dialog.dismiss()
                        }
                    })
                }
                .setNegativeButton(
                    R.string.cancel
                ) { dialog, _ ->
                    dialog.cancel()
                }
                .create().show()
        }

        return MyListsViewHolder(view)
    }

    override fun getItemCount(): Int = listItems.results?.size!!
    override fun onBindViewHolder(holder: MyListsViewHolder, position: Int) {
        adapterPos = holder.adapterPosition
        itemPos = position
        listId = holder.listId

        val listItem = listItems.results?.get(position)
        holder.bind(listItem!!)
    }
}