package com.moviesearcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.adapters.MyListAdapter
import com.moviesearcher.viewmodel.MyListViewModel

private const val TAG = "FavoritesFragment"

class MyListFragment : BaseFragment() {
    private val args by navArgs<MyListFragmentArgs>()
    private lateinit var myListRecyclerView: RecyclerView
    private lateinit var myListViewModel: MyListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_list, container, false)

        val listId = args.listId

        myListRecyclerView = view.findViewById(R.id.fragment_my_list_recycler_view)
        myListRecyclerView.layoutManager = LinearLayoutManager(context)
        myListViewModel = ViewModelProvider(this).get(MyListViewModel::class.java)

        myListViewModel.getList(listId)

        myListViewModel.myListItemLiveData.observe(
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