package com.moviesearcher.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

class ImagesFragment : Fragment() {
    private var _binding: FragmentImagesBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ImagesFragmentArgs>()

    private val movieId = args.movieId?.toLong()
    private val tvId = args.tvId?.toLong()
    private val personId = args.personId?.toLong()

    private val viewModel: BaseViewModel by viewModels()
    private val movieViewModel: MovieViewModel by viewModels { ViewModelFactory(movieId = movieId) }
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

        when {
            movieId != null -> {
                movieViewModel.getImages()
                    .observe(this, {
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
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    })
            }
            tvId != null -> {
                viewModel.getImagesByTvId(tvId)
                    .observe(viewLifecycleOwner, { imagesItems ->
                        val imagesAdapter = ImagesAdapter(imagesItems)

                        imagesRecyclerView.apply {
                            adapter = imagesAdapter
                            layoutManager = GridLayoutManager(requireContext(), 2)
                        }
                        imagesAdapter.differ.submitList(imagesItems.backdrops)
                    })
            }
            else -> {
                viewModel.getImagesByPersonId(personId!!)
                    .observe(viewLifecycleOwner, { imagesItems ->
                        val imagesAdapter = PersonImagesAdapter(imagesItems)

                        imagesRecyclerView.apply {
                            adapter = imagesAdapter
                            layoutManager = GridLayoutManager(requireContext(), 2)
                        }
                        imagesAdapter.differ.submitList(imagesItems.profiles)
                    })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}