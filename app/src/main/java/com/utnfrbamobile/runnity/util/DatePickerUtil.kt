package com.utnfrbamobile.runnity.util

import com.google.android.material.datepicker.*
import com.utnfrbamobile.runnity.R
import java.util.*

class DatePickerUtil {
    companion object {
        fun buildDatePicker(initialDate: Long): MaterialDatePicker<Long> {
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
                .setSelection(initialDate)
                .setCalendarConstraints(constraintsBuilder.build())
                .build()
        }
    }
}