package com.utnfrbamobile.runnity.util

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

fun Fragment.onBackPressedCustomAction(action: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
        override
        fun handleOnBackPressed() {
            action()
        }
    })
}