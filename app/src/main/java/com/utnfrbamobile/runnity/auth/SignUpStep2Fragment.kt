package com.utnfrbamobile.runnity.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.utnfrbamobile.runnity.databinding.FragmentSignUpStep2Binding
import java.util.*
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointForward
import com.utnfrbamobile.runnity.R
import com.utnfrbamobile.runnity.util.DatePickerUtil
import com.utnfrbamobile.runnity.util.FirebaseWrapper


class SignUpStep2Fragment : Fragment() {

    private var _binding: FragmentSignUpStep2Binding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SignUpViewModel

    private var dateSelected = MaterialDatePicker.todayInUtcMilliseconds()
    private val datePicker = DatePickerUtil.buildDatePicker(dateSelected)
    private val DATEPICKER_TAG = "BIRTHDATE_DATEPICKER"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        requireActivity().findViewById<View>(R.id.nav_view).visibility = View.GONE
        _binding = FragmentSignUpStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FirebaseWrapper.setCurrentActivity(requireActivity())
        viewModel = ViewModelProvider(requireActivity()).get(SignUpViewModel::class.java)

        binding.name.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                binding.name.clearFocus()
                showDatePicker()
                true
            }
            false
        }

        binding.birthdate.setOnClickListener {
            showDatePicker()
        }

        datePicker.addOnPositiveButtonClickListener {
            dateSelected = datePicker.selection!!
            binding.birthdate.setText(datePicker.headerText)

            binding.weight.requestFocus()
        }

        binding.continueButton.setOnClickListener {
            val name = binding.name.text.toString()
            val birthdate = binding.birthdate.text.toString()
            val weight = binding.weight.text.toString()

            when{
                name.isEmpty() -> Toast.makeText(activity, R.string.empty_name_message, Toast.LENGTH_SHORT).show()
                birthdate.isEmpty() -> Toast.makeText(activity, R.string.empty_birthdate_message, Toast.LENGTH_SHORT).show()
                weight.isEmpty() -> Toast.makeText(activity, R.string.empty_weight_message, Toast.LENGTH_SHORT).show()
                else -> FirebaseWrapper.saveProfile(viewModel.email, name, dateSelected, weight).addOnCompleteListener {
                    if (it.isSuccessful){
                        findNavController().navigate(SignUpStep2FragmentDirections.actionSignUpStep2FragmentToCompetitionMenuFragment())
                    }
                }
            }
        }
    }

    private fun showDatePicker(){
        if(datePicker.isVisible.not()){
            datePicker.show(childFragmentManager, DATEPICKER_TAG)
        }
    }
}