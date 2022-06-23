package com.moviesearcher.common

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moviesearcher.R
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.databinding.DialogPosterBinding

class PosterDialog(private val posterPath: String) : DialogFragment() {
    private var _binding: DialogPosterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogPosterBinding.inflate(LayoutInflater.from(context))
        val view = binding.root

        val dialog = MaterialAlertDialogBuilder(requireContext()).setView(view).create()
        val poster = binding.posterImageView

        Glide.with(this)
            .load(Constants.IMAGE_URL + posterPath)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .override(900, 1400)
            .into(poster)

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}