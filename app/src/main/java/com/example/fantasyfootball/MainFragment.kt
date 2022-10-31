package com.example.fantasyfootball

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fantasyfootball.databinding.MainFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class MainFragment: Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth

    /** Binding to XML layout */
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Use the provided ViewBinding class to inflate the layout.
        binding = MainFragmentBinding.inflate(inflater, container, false)

        firebaseAuth = requireNotNull(FirebaseAuth.getInstance())

        binding.loginLogin.setOnClickListener { loginUserAccount() }

        binding.register.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainFragment_to_registrationFragment
            )
        }

        binding.forgotPasswordHint.setOnClickListener {
            findNavController().navigate(
                R.id.action_mainFragment_to_forgotPasswordFragment
            )
        }

        return binding.root
    }

    private fun loginUserAccount() {
        val email: String = binding.loginEmail.text.toString()
        val password: String = binding.loginPassword.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(
                requireContext(),
                getString(R.string.login_toast),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(
                requireContext(),
                getString(R.string.password_toast),
                Toast.LENGTH_LONG
            ).show()
            return
        }

        binding.loginProgressBar.visibility = View.VISIBLE

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                binding.loginProgressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Login successful!",
                        Toast.LENGTH_LONG
                    ).show()

                    findNavController().navigate(
                        R.id.action_mainFragment_to_dashboardFragment
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Login failed. Please try again later",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

}