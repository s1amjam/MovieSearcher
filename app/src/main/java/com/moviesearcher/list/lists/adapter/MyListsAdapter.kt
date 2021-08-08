package com.moviesearcher.list.lists.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.android.material.card.MaterialCardView
import com.moviesearcher.R
import com.moviesearcher.api.Api
import com.moviesearcher.databinding.FragmentMyListsItemBinding
import com.moviesearcher.list.lists.MyListsFragmentDirections
import com.moviesearcher.list.lists.model.ListsResponse
import com.moviesearcher.list.model.Result
import com.moviesearcher.utils.Constants

class MyListsAdapter(
    private val listItems: ListsResponse,
    private val navController: NavController,
    private val sessionId: String
) : RecyclerView.Adapter<MyListsAdapter.MyListsViewHolder>() {
    private var listId: Int = -1
    lateinit var cardView: MaterialCardView

    inner class MyListsViewHolder(val binding: FragmentMyListsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val myListItemPoster: ImageView = binding.imageViewMyListsItem
        private val myListItemName: TextView = binding.textViewMyListsItemName

        fun bind(myListResultItem: Result) {
            Glide.with(this.itemView)
                .load(Constants.IMAGE_URL + myListResultItem.posterPath)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(myListItemPoster)
            myListItemName.text = myListResultItem.name
            cardView.id = myListResultItem.id!!.toInt()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyListsViewHolder {
        val binding = FragmentMyListsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        cardView = binding.materialCardViewMyListsItem

        cardView.setOnClickListener {
            navController.navigate(
                MyListsFragmentDirections.actionMyListsFragmentToMyListFragment(it.id)
            )
        }

        return MyListsViewHolder(binding)
    }

    override fun getItemCount(): Int = listItems.results?.size!!
    override fun onBindViewHolder(holder: MyListsViewHolder, position: Int) {
        val binding = holder.binding
        val imageButtonDeleteList: ImageButton = binding.imageButtonDeleteList

        //TODO: for now its bugged on API provider side
        // https://trello.com/c/slruAstb/75-return-a-proper-response-when-lists-are-deleted
        imageButtonDeleteList.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Delete list?")
                .setPositiveButton(
                    R.string.ok
                ) { dialog, _ ->
                    val deleteListResponse = Api.deleteList(listId, sessionId)

                    deleteListResponse.observe(holder.itemView.findViewTreeLifecycleOwner()!!, {
                        if (it.statusMessage == "The item/record was deleted successfully.") {
                            listItems.results!!.removeAt(holder.adapterPosition)
                            this.notifyItemRemoved(holder.adapterPosition)
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

        val listItem = listItems.results?.get(position)
        holder.bind(listItem!!)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}