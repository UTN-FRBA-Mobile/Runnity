package com.utnfrbamobile.runnity.auth

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.utnfrbamobile.runnity.databinding.FragmentSignUpStep2Binding
import java.util.*
import com.google.android.material.datepicker.CompositeDateValidator

import com.google.android.material.datepicker.DateValidatorPointForward




class SignUpStep2Fragment : Fragment() {

    private var _binding: FragmentSignUpStep2Binding? = null
    private val binding get() = _binding!!
    private val datePicker = buildDatePicker()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentSignUpStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            binding.birthdate.setText(datePicker.headerText)
            binding.weight.requestFocus()
        }

        binding.continueButton.setOnClickListener {
            val username = binding.name.text.toString()
            val birthdate = binding.birthdate.text.toString()
            val weight = binding.weight.text.toString()

            when{
                username.isEmpty() -> Toast.makeText(activity, "Ingrese su nombre", Toast.LENGTH_SHORT).show()
                birthdate.isEmpty() -> Toast.makeText(activity, "Ingrese su fecha de nacimiento", Toast.LENGTH_SHORT).show()
                weight.isEmpty() -> Toast.makeText(activity, "Ingrese su peso", Toast.LENGTH_SHORT).show()
                else -> findNavController().navigate(SignUpStep2FragmentDirections.actionSignUpStep2FragmentToMapFragment())
            }
        }
    }

    private fun buildDatePicker(): MaterialDatePicker<Long>{
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        calendar.timeInMillis = today
        calendar[Calendar.YEAR] = calendar[Calendar.YEAR] - 120
        val a120YearsAgo = calendar.timeInMillis

        val validators = CompositeDateValidator.allOf(listOf(
            DateValidatorPointForward.from(a120YearsAgo),
            DateValidatorPointBackward.now()
        ))

        val constraintsBuilder = CalendarConstraints.Builder()
            .setStart(a120YearsAgo)
            .setEnd(today)
            .setValidator(validators)

        return MaterialDatePicker.Builder.datePicker()
            .setTitleText("Fecha de nacimiento")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintsBuilder.build())
            .build()
    }

    private fun showDatePicker(){
        datePicker.show(childFragmentManager, "BIRTHDATE_DATEPICKER")
    }
}