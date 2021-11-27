package com.utnfrbamobile.runnity.auth

import androidx.lifecycle.ViewModel
import com.google.android.material.datepicker.MaterialDatePicker

class SignUpViewModel : ViewModel() {
    var email : String = "";
    var password : String = "";
    var name : String = "";
    var birthdate : Long = MaterialDatePicker.todayInUtcMilliseconds();
    var weight: String = ""
}