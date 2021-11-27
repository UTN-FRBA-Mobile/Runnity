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
import com.google.firebase.firestore.FirebaseFirestore
import com.utnfrbamobile.runnity.R
import com.utnfrbamobile.runnity.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SignUpViewModel

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        requireActivity().findViewById<View>(R.id.nav_view).visibility = View.GONE
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(SignUpViewModel::class.java)

        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            when{
                email.isEmpty() -> Toast.makeText(activity, "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show()
                password.isEmpty() -> Toast.makeText(activity, "Ingrese su contraseña", Toast.LENGTH_SHORT).show()
                else -> login(email, password)
            }
        }

        binding.signUpButton.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpStep1Fragment())
        }
    }

    private fun login(email: String, password: String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful){
                db.collection("users").document(email).get().addOnSuccessListener {
                    viewModel.email = email
                    viewModel.name = it.get("name") as String
                    viewModel.birthdate = it.get("birthdate") as String
                    viewModel.weight = it.get("weight") as String

                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToCompetitionMenuFragment())
                }
            } else {
                Toast.makeText(activity, "Hubo un problema al loguearse, revise las credenciales", Toast.LENGTH_SHORT).show()
            }
        }
    }
}