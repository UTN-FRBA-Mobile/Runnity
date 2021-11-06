package com.utnfrbamobile.runnity.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.utnfrbamobile.runnity.databinding.FragmentSignUpStep2Binding

class SignUpStep2Fragment : Fragment() {

    private var _binding: FragmentSignUpStep2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentSignUpStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.continueButton.setOnClickListener {
//            val username = binding.username.text.toString()
//            val password = binding.password.text.toString()
//
//            when{
//                username.isEmpty() -> Toast.makeText(activity, "Ingrese su nombre de usuario", Toast.LENGTH_SHORT).show()
//                password.isEmpty() -> Toast.makeText(activity, "Ingrese su password", Toast.LENGTH_SHORT).show()
//                else -> findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMapFragment())
//            }
//        }
    }
}