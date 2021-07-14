package com.moviesearcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.adapters.MyListsAdapter
import com.moviesearcher.viewmodel.MyListsViewModel

private const val TAG = "MyListsFragment"

class MyListsFragment : BaseFragment() {
    private lateinit var myListsRecyclerView: RecyclerView
    private val myListsViewModel: MyListsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_lists, container, false)

        myListsRecyclerView = view.findViewById(R.id.fragment_my_lists_recycler_view)
        myListsRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myListsViewModel.getLists(accountId, sessionId, 1).observe(
            viewLifecycleOwner,
            { myListItems ->
                myListsRecyclerView.adapter =
                    MyListsAdapter(myListItems, findNavController(), sessionId)
            })
    }
}