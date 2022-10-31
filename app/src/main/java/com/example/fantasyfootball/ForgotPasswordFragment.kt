package com.example.fantasyfootball

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fantasyfootball.databinding.ForgotPasswordFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordFragment: Fragment() {

    private lateinit var auth: FirebaseAuth

    /** Binding to XML layout */
    private lateinit var binding: ForgotPasswordFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Use the provided ViewBinding class to inflate the layout.
        binding = ForgotPasswordFragmentBinding.inflate(inflater, container, false)

        auth = requireNotNull(FirebaseAuth.getInstance())

        binding.fpwButton.setOnClickListener { resetPassword() }

        // Return the root view.
        return binding.root
    }

    private fun resetPassword() {
        // Check if email in email is in database, if so, send reset password email
        val email: String = binding.fpwEmail.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.invalid_email),
                Toast.LENGTH_LONG
            ).show()
        } else {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.successful_reset),
                            Toast.LENGTH_LONG
                        ).show()

                        findNavController().navigate(
                            R.id.action_forgotPasswordFragment_to_mainFragment
                        )
                    } else {
                        Toast.makeText(
                            requireContext(),
                            task.exception!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}