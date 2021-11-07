package com.utnfrbamobile.runnity.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.utnfrbamobile.runnity.R
import com.utnfrbamobile.runnity.databinding.FragmentSignUpStep1Binding

class SignUpStep1Fragment : Fragment() {

    private var _binding: FragmentSignUpStep1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        requireActivity().findViewById<View>(R.id.nav_view).visibility = View.GONE
        _binding = FragmentSignUpStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpButton.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            when{
                username.isEmpty() -> Toast.makeText(activity, "Ingrese su nombre de usuario", Toast.LENGTH_SHORT).show()
                password.isEmpty() -> Toast.makeText(activity, "Ingrese su contraseña", Toast.LENGTH_SHORT).show()
                else -> findNavController().navigate(SignUpStep1FragmentDirections.actionSignUpStep1FragmentToSignUpStep2Fragment())
            }
        }
    }
}