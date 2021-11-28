package com.utnfrbamobile.runnity.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.utnfrbamobile.runnity.R
import com.utnfrbamobile.runnity.databinding.FragmentSignUpStep1Binding

class SignUpStep1Fragment : Fragment() {

    private var _binding: FragmentSignUpStep1Binding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentSignUpStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(SignUpViewModel::class.java)

        binding.signUpButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            when{
                email.isEmpty() -> Toast.makeText(activity, R.string.empty_email_message, Toast.LENGTH_SHORT).show()
                isValidEmail(email).not() -> Toast.makeText(activity, R.string.invalid_email_message, Toast.LENGTH_SHORT).show()
                password.isEmpty() -> Toast.makeText(activity, R.string.empty_password_message, Toast.LENGTH_SHORT).show()
                password.length < 8 -> Toast.makeText(activity, R.string.invalid_password_message, Toast.LENGTH_SHORT).show()
                else -> completeSignUp(email, password)
            }
        }
    }

    private fun isValidEmail(email: String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private fun completeSignUp(email: String, password: String){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                viewModel.email = email
                findNavController().navigate(SignUpStep1FragmentDirections.actionSignUpStep1FragmentToSignUpStep2Fragment())
            } else {
                Toast.makeText(activity, R.string.signup_error_message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}