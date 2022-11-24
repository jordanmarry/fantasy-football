package com.example.fantasyfootball

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fantasyfootball.databinding.RegistrationFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistrationFragment: Fragment() {

    private var validator = Validators()
    private var writer = ReadAndWriteData()
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    /** Binding to XML layout */
    private lateinit var binding: RegistrationFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Use the provided ViewBinding class to inflate the layout.
        binding = RegistrationFragmentBinding.inflate(inflater, container, false)

        auth = requireNotNull(FirebaseAuth.getInstance())

        binding.registrationLogin.setOnClickListener { registerNewUser() }

        // Initialize database
        database = FirebaseDatabase.getInstance().getReference("users")

        // Return the root view.
        return binding.root
    }

    private fun registerNewUser() {
        val email: String = binding.registrationEmail.text.toString()
        val password: String = binding.registrationPassword.text.toString()

        if (!validator.validEmail(email)) {
            Toast.makeText(
                requireContext(),
                getString(R.string.invalid_email),
                Toast.LENGTH_LONG
            ).show()

            return
        }

        if (!validator.validPassword(password)) {
            Toast.makeText(
                requireContext(),
                getString(R.string.invalid_password),
                Toast.LENGTH_LONG
            ).show()

            return
        }

        binding.registrationProgressBar.visibility = View.VISIBLE

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                binding.registrationProgressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    // Getting a unique id using push().getKey() method
                    // it will create a unique id and we will use it as
                    // the Primary Key for our Author.
                    val id = database.push().key

                    // Add user to database
                    writer.writeUser(id!!, email)
                    // Make success message
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.register_success_string),
                        Toast.LENGTH_LONG
                    ).show()

                    findNavController().navigate(
                        R.id.action_registrationFragment_to_dashboardFragment
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.register_failed_string),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

}