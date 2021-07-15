package com.moviesearcher.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.R
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.list.adapter.MyListAdapter
import com.moviesearcher.list.viewmodel.MyListViewModel

private const val TAG = "FavoritesFragment"

class MyListFragment : BaseFragment() {
    private val args by navArgs<MyListFragmentArgs>()
    private lateinit var myListRecyclerView: RecyclerView
    private val myListViewModel: MyListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_list, container, false)

        val listId = args.listId

        myListRecyclerView = view.findViewById(R.id.fragment_my_list_recycler_view)
        myListRecyclerView.layoutManager = LinearLayoutManager(context)

        myListViewModel.getList(listId).observe(
            viewLifecycleOwner,
            { myListItems ->
                myListRecyclerView.adapter = MyListAdapter(
                    myListItems,
                    findNavController(),
                    args.listId,
                    sessionId
                )
            })

        return view
    }
}