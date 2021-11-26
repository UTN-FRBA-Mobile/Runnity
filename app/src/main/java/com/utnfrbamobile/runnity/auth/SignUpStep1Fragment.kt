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
import com.utnfrbamobile.runnity.databinding.FragmentSignUpStep1Binding

class SignUpStep1Fragment : Fragment() {

    private var _binding: FragmentSignUpStep1Binding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        requireActivity().findViewById<View>(R.id.nav_view).visibility = View.GONE
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
                email.isEmpty() -> Toast.makeText(activity, "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show()
                isValidEmail(email).not() -> Toast.makeText(activity, "El correo electrónico es inválido", Toast.LENGTH_SHORT).show()
                password.isEmpty() -> Toast.makeText(activity, "Ingrese su contraseña", Toast.LENGTH_SHORT).show()
                password.length < 8 -> Toast.makeText(activity, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
                else -> {
                    viewModel.email = email
                    viewModel.password = password
                    findNavController().navigate(SignUpStep1FragmentDirections.actionSignUpStep1FragmentToSignUpStep2Fragment())
                }
            }
        }
    }

    private fun isValidEmail(email: String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}