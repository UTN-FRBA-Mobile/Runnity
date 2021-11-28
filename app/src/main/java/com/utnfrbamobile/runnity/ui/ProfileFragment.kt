package com.utnfrbamobile.runnity.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.*
import com.google.firebase.firestore.FirebaseFirestore
import com.utnfrbamobile.runnity.R
import com.utnfrbamobile.runnity.auth.SignUpViewModel
import com.utnfrbamobile.runnity.databinding.FragmentProfileBinding
import java.util.*

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SignUpViewModel

    private val db = FirebaseFirestore.getInstance()

    private var dateSelected = MaterialDatePicker.todayInUtcMilliseconds()
    private lateinit var datePicker: MaterialDatePicker<Long>
    private val DATEPICKER_TAG = "BIRTHDATE_DATEPICKER"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(SignUpViewModel::class.java)

        dateSelected = viewModel.birthdate
        datePicker = buildDatePicker()

        binding.name.setText(viewModel.name)
        binding.birthdate.setText(viewModel.birthdate.toString())
        binding.weight.setText(viewModel.weight)

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
                else -> {
                    viewModel.name = name
                    viewModel.birthdate = dateSelected
                    viewModel.weight = weight
                    updateProfile()
                }
            }
        }
    }

    private fun buildDatePicker(): MaterialDatePicker<Long> {
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
            .setTitleText(R.string.birthdate)
            .setSelection(dateSelected)
            .setCalendarConstraints(constraintsBuilder.build())
            .build()
    }

    private fun showDatePicker(){
        if(datePicker.isVisible.not()){
            datePicker.show(childFragmentManager, DATEPICKER_TAG)
        }
    }

    private fun updateProfile(){
        db.collection("users").document(viewModel.email).set(
            hashMapOf(
                "name" to viewModel.name,
                "birthdate" to viewModel.birthdate,
                "weight" to viewModel.weight
            )
        ).addOnCompleteListener {
            Toast.makeText(activity, R.string.profile_updated_ok_message, Toast.LENGTH_SHORT).show()

            Toast.makeText(
                activity,
                if (it.isSuccessful) R.string.profile_updated_ok_message else R.string.signup_error_message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}