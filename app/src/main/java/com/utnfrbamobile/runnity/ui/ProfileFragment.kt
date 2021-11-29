package com.utnfrbamobile.runnity.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.*
import com.utnfrbamobile.runnity.R
import com.utnfrbamobile.runnity.auth.SignUpViewModel
import com.utnfrbamobile.runnity.databinding.FragmentProfileBinding
import com.utnfrbamobile.runnity.util.DatePickerUtil
import com.utnfrbamobile.runnity.util.FirebaseWrapper

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SignUpViewModel

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

        FirebaseWrapper.setCurrentActivity(requireActivity())

        viewModel = ViewModelProvider(requireActivity()).get(SignUpViewModel::class.java)

        dateSelected = viewModel.birthdate
        datePicker = DatePickerUtil.buildDatePicker(dateSelected)

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
                    FirebaseWrapper.saveProfile(viewModel.email, name, dateSelected, weight)
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