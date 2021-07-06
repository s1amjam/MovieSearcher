package com.moviesearcher

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.list.CreateNewList
import com.moviesearcher.utils.EncryptedSharedPrefs

class CreateNewListDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(requireContext())
            .create()

        val view = layoutInflater.inflate(R.layout.dialog_create_new_list, null)

        val name: EditText = view.findViewById(R.id.edit_text_list_name)
        val description: EditText = view.findViewById(R.id.edit_text_list_description)
        val buttonCreate: Button = view.findViewById(R.id.button_create_new_list)
        val buttonCancel: Button = view.findViewById(R.id.button_cancel_create_new_list)

        dialog.setView(view)

        if (name.text.toString() == "") {
            buttonCreate.isEnabled = false
        }

        buttonCreate.setOnClickListener {
            val sessionId = EncryptedSharedPrefs.sharedPrefs(requireContext())
                .getString("sessionId", "").toString()

            if (name.text.toString() == "") {
                Toast.makeText(requireContext(), "Name can't be empty", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Api.createNewList(
                    sessionId,
                    CreateNewList(name.text.toString(), description.text.toString(), "en")
                ).observe(
                    this, {
                        if (it.success == true) {
                            dialog.dismiss()
                        }
                    })
            }
        }

        buttonCancel.setOnClickListener { dialog.cancel() }

        return dialog
    }
}