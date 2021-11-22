package com.moviesearcher.list.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.databinding.FragmentMyListsBinding
import com.moviesearcher.list.lists.adapter.MyListsAdapter
import com.moviesearcher.list.lists.viewmodel.MyListsViewModel

private const val TAG = "MyListsFragment"

class MyListsFragment : BaseFragment() {
    private var _binding: FragmentMyListsBinding? = null
    private val binding get() = _binding!!

    private lateinit var myListsRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private val myListsViewModel: MyListsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyListsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBarLists
        progressBar.visibility = View.VISIBLE
        myListsRecyclerView = binding.fragmentMyListsRecyclerView
        myListsRecyclerView.layoutManager = LinearLayoutManager(context)
        myListsRecyclerView.visibility = View.INVISIBLE

        myListsViewModel.getLists(accountId, sessionId, 1).observe(
            viewLifecycleOwner,
            { myListItems ->
                myListsRecyclerView.adapter =
                    MyListsAdapter(myListItems, findNavController(), sessionId)
                progressBar.visibility = View.GONE
                myListsRecyclerView.visibility = View.VISIBLE
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}