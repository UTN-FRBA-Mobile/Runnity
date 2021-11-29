package com.utnfrbamobile.runnity.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.utnfrbamobile.runnity.R
import com.utnfrbamobile.runnity.databinding.FragmentSignUpStep1Binding
import com.utnfrbamobile.runnity.util.FirebaseWrapper

class SignUpStep1Fragment : Fragment() {

    private var _binding: FragmentSignUpStep1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentSignUpStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseWrapper.setCurrentActivity(requireActivity())

        binding.signUpButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            when{
                email.isEmpty() -> Toast.makeText(activity, R.string.empty_email_message, Toast.LENGTH_SHORT).show()
                isValidEmail(email).not() -> Toast.makeText(activity, R.string.invalid_email_message, Toast.LENGTH_SHORT).show()
                password.isEmpty() -> Toast.makeText(activity, R.string.empty_password_message, Toast.LENGTH_SHORT).show()
                password.length < 8 -> Toast.makeText(activity, R.string.invalid_password_message, Toast.LENGTH_SHORT).show()
                else -> FirebaseWrapper.createUser(email, password)
            }
        }
    }

    private fun isValidEmail(email: String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}