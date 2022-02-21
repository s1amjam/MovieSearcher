package com.moviesearcher.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.common.utils.Status
import com.moviesearcher.common.viewmodel.ViewModelFactory
import com.moviesearcher.databinding.FragmentMyListBinding
import com.moviesearcher.list.adapter.MyListAdapter

private const val TAG = "FavoritesFragment"

class MyListFragment : BaseFragment() {
    private var _binding: FragmentMyListBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<MyListFragmentArgs>()

    private lateinit var progressBar: ProgressBar
    private lateinit var myListRecyclerView: RecyclerView
    private lateinit var emptyListTextView: TextView

    private lateinit var viewModel: ListViewModel

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

        myListRecyclerView = binding.fragmentMyListRecyclerView
        progressBar = binding.progressBarList
        emptyListTextView = binding.emptyListTv

        setupViewModel()

        viewModel.getMyList().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { myListItems ->
                        if (myListItems.itemCount == 0) {
                            progressBar.visibility = View.GONE
                            emptyListTextView.visibility = View.VISIBLE
                        } else {
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
                            emptyListTextView.visibility = View.GONE
                            myListRecyclerView.visibility = View.VISIBLE
                        }
                    }
                }
                Status.LOADING -> {
                    myListRecyclerView.visibility = View.GONE
                    emptyListTextView.visibility = View.GONE
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
            this,
            ViewModelFactory(listId = args.listId)
        ).get(ListViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}