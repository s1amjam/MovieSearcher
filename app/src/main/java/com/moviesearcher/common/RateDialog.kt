package com.moviesearcher.common

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moviesearcher.common.model.rate.AccountStatesResponse
import com.moviesearcher.common.model.rate.Rated
import com.moviesearcher.common.utils.Resource
import com.moviesearcher.common.utils.Status
import com.moviesearcher.common.viewmodel.RateViewModel
import com.moviesearcher.databinding.RateDialogBinding
import com.moviesearcher.movie.MovieViewModel
import com.moviesearcher.watchlist.common.viewmodel.ERROR_MESSAGE

class RateDialog(
    private val id: Long,
    private val sessionId: String,
    private val movieViewModel: MovieViewModel
) : DialogFragment() {
    private var _binding: RateDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = RateDialogBinding.inflate(LayoutInflater.from(context))
        val view = binding.root
        val rateButton = binding.rateButton
        val cancelButton = binding.cancelButton
        val dialog = MaterialAlertDialogBuilder(requireContext()).setView(view).create()
        val rateViewModel: RateViewModel by viewModels()

        rateButton.setOnClickListener {
            rateViewModel.postMovieRate(id, sessionId, Rated(binding.ratingBar.rating))
                .observe(this) {
                    when (it.status) {
                        Status.SUCCESS -> {
                            it.data?.let { response ->
                                if (response.statusCode == 12 || response.statusCode == 1) {
                                    movieViewModel.getAccountStates().postValue(
                                        Resource.success(
                                            AccountStatesResponse(
                                                rated = Rated(binding.ratingBar.rating)
                                            )
                                        )
                                    )

                                    dialog.dismiss()
                                }
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

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}