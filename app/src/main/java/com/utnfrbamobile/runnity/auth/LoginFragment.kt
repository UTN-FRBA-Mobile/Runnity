package com.utnfrbamobile.runnity.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.utnfrbamobile.runnity.R
import com.utnfrbamobile.runnity.databinding.FragmentLoginBinding
import com.utnfrbamobile.runnity.util.FirebaseWrapper

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        requireActivity().findViewById<View>(R.id.nav_view).visibility = View.GONE
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseWrapper.setCurrentActivity(requireActivity())
        FirebaseWrapper.setViewModel(ViewModelProvider(requireActivity()).get(SignUpViewModel::class.java))
        FirebaseWrapper.setNavController(findNavController())

        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            R.string.empty_email_message

            when{
                email.isEmpty() -> Toast.makeText(activity, R.string.empty_email_message, Toast.LENGTH_SHORT).show()
                password.isEmpty() -> Toast.makeText(activity, R.string.empty_password_message, Toast.LENGTH_SHORT).show()
                else -> FirebaseWrapper.authenticateUser(email, password)
            }
        }

        binding.signUpButton.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpStep1Fragment())
        }
    }
}