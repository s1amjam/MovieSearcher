package com.moviesearcher.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.common.utils.Status
import com.moviesearcher.common.viewmodel.BaseViewModel
import com.moviesearcher.common.viewmodel.ViewModelFactory
import com.moviesearcher.databinding.FragmentImagesBinding
import com.moviesearcher.movie.MovieViewModel
import com.moviesearcher.movie.adapter.images.ImagesAdapter
import com.moviesearcher.person.adapter.combinedcredits.images.PersonImagesAdapter
import com.moviesearcher.tv.TvViewModel

class ImagesFragment : BaseFragment() {
    private var _binding: FragmentImagesBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ImagesFragmentArgs>()

    private val viewModel: BaseViewModel by viewModels()
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var tvViewModel: TvViewModel
    private lateinit var imagesRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imagesRecyclerView = binding.photosRecyclerView

        setupViewModel()
        setupUi()
    }

    private fun setupUi() {
        val movieId = args.movieId?.toLong()
        val tvId = args.tvId?.toLong()
        val personId = args.personId?.toLong()

        when {
            movieId != null -> {
                movieViewModel.getImages().observe(viewLifecycleOwner) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            it.data?.let { imagesItems ->
                                val imagesAdapter = ImagesAdapter(imagesItems)

                                imagesRecyclerView.apply {
                                    adapter = imagesAdapter
                                    layoutManager = GridLayoutManager(requireContext(), 2)
                                }
                                imagesAdapter.differ.submitList(imagesItems.backdrops)
                            }
                        }
                        Status.LOADING -> {
                        }
                        Status.ERROR -> {
                            Toast.makeText(
                                requireContext(),
                                ERROR_MESSAGE.format(it.message),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
            tvId != null -> {
                tvViewModel.getImages().observe(viewLifecycleOwner) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            it.data?.let { imagesItems ->
                                val imagesAdapter = ImagesAdapter(imagesItems)

                                imagesRecyclerView.apply {
                                    adapter = imagesAdapter
                                    layoutManager = GridLayoutManager(requireContext(), 2)
                                }
                                imagesAdapter.differ.submitList(imagesItems.backdrops)
                            }
                        }
                        Status.LOADING -> {
                        }
                        Status.ERROR -> {
                            Toast.makeText(
                                requireContext(),
                                ERROR_MESSAGE.format(it.message),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
            personId != null -> {
                viewModel.getImagesByPersonId(personId)
                    .observe(viewLifecycleOwner) { imagesItems ->
                        val imagesAdapter = PersonImagesAdapter(imagesItems)

                        imagesRecyclerView.apply {
                            adapter = imagesAdapter
                            layoutManager = GridLayoutManager(requireContext(), 2)
                        }
                        imagesAdapter.differ.submitList(imagesItems.profiles)
                    }
            }
        }
    }

    private fun setupViewModel() {
        if (args.movieId != null) {
            movieViewModel = ViewModelProvider(
                this, ViewModelFactory(
                    movieId = args.movieId?.toLong()
                )
            ).get(MovieViewModel::class.java)
        } else if (args.tvId != null) {
            tvViewModel = ViewModelProvider(
                this, ViewModelFactory(
                    tvId = args.tvId?.toLong()
                )
            ).get(TvViewModel::class.java)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}