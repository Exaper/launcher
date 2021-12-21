package com.exaper.launcher.ui

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.exaper.launcher.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RestrictedApplicationDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?) =
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.dialog_restricted_application_title)
            .setMessage(R.string.dialog_restricted_application_message)
            .setPositiveButton(R.string.ok, null)
            .create()

    companion object {
        const val TAG = "RestrictedApplicationDialog"
    }
}
