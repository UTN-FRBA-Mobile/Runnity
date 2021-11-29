package com.utnfrbamobile.runnity.domain

import com.google.android.material.datepicker.MaterialDatePicker

data class User(
    var email : String = "",
    var name : String = "",
    var birthdate : Long = MaterialDatePicker.todayInUtcMilliseconds(),
    var weight: String = ""
)