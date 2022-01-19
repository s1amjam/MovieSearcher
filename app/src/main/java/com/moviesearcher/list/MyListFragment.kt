package com.moviesearcher.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.common.viewmodel.BaseViewModel
import com.moviesearcher.databinding.FragmentMyListBinding
import com.moviesearcher.list.adapter.MyListAdapter

private const val TAG = "FavoritesFragment"

class MyListFragment : BaseFragment() {
    private var _binding: FragmentMyListBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<MyListFragmentArgs>()

    private lateinit var progressBar: ProgressBar
    private lateinit var myListRecyclerView: RecyclerView

    private val viewModel: BaseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listId = args.listId

        myListRecyclerView = binding.fragmentMyListRecyclerView
        myListRecyclerView.visibility = View.INVISIBLE
        progressBar = binding.progressBarList
        progressBar.visibility = View.VISIBLE

        viewModel.getList(listId).observe(
            viewLifecycleOwner,
            { myListItems ->
                val myListAdapter = MyListAdapter(
                    myListItems,
                    findNavController(),
                    args.listId,
                    sessionId
                )
                binding.listTitleTextView.text = myListItems.name

                myListRecyclerView.apply {
                    adapter = myListAdapter
                    layoutManager = LinearLayoutManager(context)
                }
                myListAdapter.differ.submitList(myListItems.items)

                progressBar.visibility = View.GONE
                myListRecyclerView.visibility = View.VISIBLE
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}