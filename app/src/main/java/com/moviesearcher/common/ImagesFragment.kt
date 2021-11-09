package com.moviesearcher.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesearcher.databinding.FragmentImagesBinding
import com.moviesearcher.movie.adapter.images.ImagesAdapter
import com.moviesearcher.movie.viewmodel.images.ImagesViewModel

class ImagesFragment : Fragment() {
    private var _binding: FragmentImagesBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ImagesFragmentArgs>()
    private val imagesViewModel: ImagesViewModel by viewModels()
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

        val movieId = args.movieId
        imagesRecyclerView = binding.photosRecyclerView

        imagesViewModel.getImagesByMovieId(movieId)
            .observe(viewLifecycleOwner, { imagesItems ->
                val imagesAdapter = ImagesAdapter(imagesItems)

                imagesRecyclerView.apply {
                    adapter = imagesAdapter
                    layoutManager = GridLayoutManager(requireContext(), 2)
                }
                imagesAdapter.differ.submitList(imagesItems.backdrops)
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}