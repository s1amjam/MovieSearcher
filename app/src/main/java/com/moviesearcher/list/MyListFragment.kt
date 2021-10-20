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
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.databinding.FragmentMyListBinding
import com.moviesearcher.list.adapter.MyListAdapter
import com.moviesearcher.list.viewmodel.MyListViewModel

private const val TAG = "FavoritesFragment"

class MyListFragment : BaseFragment() {
    private var _binding: FragmentMyListBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<MyListFragmentArgs>()

    private lateinit var myListRecyclerView: RecyclerView
    private val myListViewModel: MyListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyListBinding.inflate(inflater, container, false)
        val view = binding.root
        val listId = args.listId

        myListRecyclerView = binding.fragmentMyListRecyclerView
        myListRecyclerView.layoutManager = LinearLayoutManager(context)

        myListViewModel.getList(listId).observe(
            viewLifecycleOwner,
            { myListItems ->
                binding.listTitleTextView.text = myListItems.name
                
                myListRecyclerView.adapter = MyListAdapter(
                    myListItems,
                    findNavController(),
                    args.listId,
                    sessionId
                )
            })

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}