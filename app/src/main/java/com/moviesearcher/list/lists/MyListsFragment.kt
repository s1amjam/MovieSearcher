package com.moviesearcher.list.lists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.credentials.CredentialsHolder
import com.moviesearcher.common.utils.Constants.ERROR_MESSAGE
import com.moviesearcher.common.utils.Status
import com.moviesearcher.databinding.FragmentMyListsBinding
import com.moviesearcher.list.lists.adapter.MyListsAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyListsFragment : Fragment() {

    private var _binding: FragmentMyListsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var credentialsHolder: CredentialsHolder
    private val sessionId: String
        get() = credentialsHolder.getSessionId()

    private val viewModel by viewModels<ListsViewModel>()

    private lateinit var myListsRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var noListsTv: TextView

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

        viewModel.getLists().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { myListItems ->
                        if (myListItems.totalResults == 0) {
                            progressBar.visibility = View.GONE
                            noListsTv.visibility = View.VISIBLE
                        } else {
                            myListsRecyclerView.adapter =
                                MyListsAdapter(
                                    myListItems,
                                    findNavController(),
                                    sessionId
                                )

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
                    Toast.makeText(
                        requireContext(),
                        ERROR_MESSAGE.format(it.message),
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}