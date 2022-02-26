package com.moviesearcher.list

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import com.moviesearcher.api.Api
import com.moviesearcher.common.utils.EncryptedSharedPrefs
import com.moviesearcher.databinding.DialogCreateNewListBinding
import com.moviesearcher.list.model.CreateNewList

class CreateNewListDialog : DialogFragment() {
    private var _binding: DialogCreateNewListBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(requireContext())
            .create()

        _binding = DialogCreateNewListBinding.inflate(LayoutInflater.from(context))
        val view = binding.root

        val listName: EditText = binding.editTextListName
        val listDescription: EditText = binding.editTextListDescription
        val buttonCreate: Button = binding.buttonCreateNewList
        val buttonCancel: Button = binding.buttonCancelCreateNewList

        dialog.setView(view)

        if (listName.text.toString() == "") {
            buttonCreate.isEnabled = false
        }

        listName.doAfterTextChanged { text ->
            buttonCreate.isEnabled = text.toString() != ""
        }

        buttonCreate.setOnClickListener {
            val sessionId = EncryptedSharedPrefs.sharedPrefs(requireContext())
                .getString("sessionId", "").toString()

            Api.createNewList(
                sessionId,
                CreateNewList(listName.text.toString(), listDescription.text.toString(), "en")
            ).observe(
                this
            ) {
                if (it.success == true) {
                    Toast.makeText(requireContext(), "New List created", Toast.LENGTH_SHORT)
                        .show()
                    dialog.dismiss()
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