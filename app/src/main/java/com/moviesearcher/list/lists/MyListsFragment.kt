package com.moviesearcher.list.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.common.utils.Status
import com.moviesearcher.common.viewmodel.ViewModelFactory
import com.moviesearcher.databinding.FragmentMyListsBinding
import com.moviesearcher.list.lists.adapter.MyListsAdapter

private const val TAG = "MyListsFragment"

class MyListsFragment : BaseFragment() {
    private var _binding: FragmentMyListsBinding? = null
    private val binding get() = _binding!!

    private lateinit var myListsRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var noListsTv: TextView

    private lateinit var viewModel: ListsViewModel

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
        myListsRecyclerView = binding.fragmentMyListsRecyclerView
        myListsRecyclerView.layoutManager = LinearLayoutManager(context)
        noListsTv = binding.noListsTv

        setupViewModel()

        viewModel.getLists().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { myListItems ->
                        if (myListItems.totalResults == 0) {
                            progressBar.visibility = View.GONE
                            noListsTv.visibility = View.VISIBLE
                        } else {
                            myListsRecyclerView.adapter =
                                MyListsAdapter(myListItems, findNavController(), sessionId)

                            progressBar.visibility = View.GONE
                            noListsTv.visibility = View.GONE
                            myListsRecyclerView.visibility = View.VISIBLE
                        }
                    }
                }
                Status.LOADING -> {
                    myListsRecyclerView.visibility = View.GONE
                    noListsTv.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        ERROR_MESSAGE.format(it.message),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory(
                sessionId, accountId, page = 1
            )
        ).get(ListsViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}