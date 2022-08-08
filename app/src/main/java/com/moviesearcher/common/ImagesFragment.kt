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
import com.moviesearcher.common.utils.Constants.ERROR_MESSAGE
import com.moviesearcher.common.utils.Status
import com.moviesearcher.databinding.FragmentImagesBinding
import com.moviesearcher.movie.MovieViewModel
import com.moviesearcher.movie.adapter.images.ImagesAdapter
import com.moviesearcher.person.PersonViewModel
import com.moviesearcher.person.adapter.combinedcredits.images.PersonImagesAdapter
import com.moviesearcher.tv.TvViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagesFragment : Fragment() {
    private var _binding: FragmentImagesBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ImagesFragmentArgs>()

    private val movieViewModel by viewModels<MovieViewModel>()
    private val tvViewModel by viewModels<TvViewModel>()
    private val personViewModel by viewModels<PersonViewModel>()

    private lateinit var imagesRecyclerView: RecyclerView

    private lateinit var imagesAdapter: ImagesAdapter

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

        setupMovieAdapter()
        setupObservers()
    }

    private fun setupObservers() {
        val movieId = args.movieId
        val tvId = args.tvId
        val personId = args.personId

        when {
            movieId != 0L -> {
                movieViewModel.getImages().observe(viewLifecycleOwner) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            it.data?.let { imagesItems ->
                                imagesAdapter.submitList(imagesItems.backdrops)
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
            tvId != 0L -> {
                tvViewModel.getImages().observe(viewLifecycleOwner) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            it.data?.let { imagesItems ->
                                imagesAdapter.submitList(imagesItems.backdrops)
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
            personId != 0L -> {
                personViewModel.getPersonImages()
                    .observe(viewLifecycleOwner) {
                        when (it.status) {
                            Status.SUCCESS -> {
                                it.data?.let { imagesItems ->
                                    val imagesAdapter = PersonImagesAdapter(imagesItems)

                                    imagesAdapter.differ.submitList(imagesItems.profiles)
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
        }
    }

    private fun setupMovieAdapter() {
        imagesAdapter = ImagesAdapter()

        imagesRecyclerView.adapter = imagesAdapter
        imagesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}