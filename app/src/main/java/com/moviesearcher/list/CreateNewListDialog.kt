package com.moviesearcher.list

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.moviesearcher.common.credentials.CredentialsHolder
import com.moviesearcher.common.utils.Status
import com.moviesearcher.databinding.DialogCreateNewListBinding
import com.moviesearcher.list.model.CreateNewList
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateNewListDialog(private val viewModel: ListViewModel) : DialogFragment() {
    private var _binding: DialogCreateNewListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var credentialsHolder: CredentialsHolder
    private val sessionId: String
        get() = credentialsHolder.getSessionId()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogCreateNewListBinding.inflate(layoutInflater)
        val view = binding.root

        val dialog = MaterialAlertDialogBuilder(requireContext()).create()
        dialog.setView(view)

        val listName: EditText = binding.editTextListName
        val listDescription: EditText = binding.editTextListDescription
        val buttonCreate: Button = binding.buttonCreateNewList
        val buttonCancel: Button = binding.buttonCancelCreateNewList

        if (listName.text.toString() == "") {
            buttonCreate.isEnabled = false
        }

        listName.doAfterTextChanged { text ->
            buttonCreate.isEnabled = text.toString() != ""
        }

        buttonCreate.setOnClickListener {
            val createNewList =
                CreateNewList(listName.text.toString(), listDescription.text.toString(), "en")

            viewModel.createNewList(sessionId, createNewList).observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let {
                            Toast.makeText(
                                requireContext(),
                                "New List created",
                                Toast.LENGTH_SHORT
                            ).show()

                            dialog.dismiss()
                        }
                    }
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                        Toast.makeText(
                            requireContext(),
                            "Error while creating new list",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        buttonCancel.setOnClickListener { dialog.cancel() }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}