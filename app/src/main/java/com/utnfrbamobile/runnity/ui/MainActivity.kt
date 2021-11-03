package com.utnfrbamobile.runnity.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.utnfrbamobile.runnity.R
import com.utnfrbamobile.runnity.auth.LoginFragment
import com.utnfrbamobile.runnity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), LoginFragment.OnFragmentInteractionListener {
    private lateinit var loginFragment: Fragment
    private lateinit var mapFragment: Fragment

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            loginFragment = LoginFragment()

            supportFragmentManager.beginTransaction()
                .replace(R.id.container, loginFragment)
                .commitNow()
        }
    }

    override fun onLogin(username: String, password: String) {
        mapFragment = MapFragment()

        supportFragmentManager.beginTransaction()
            .remove(loginFragment)
            .add(R.id.container, mapFragment)
            .commitNow()
    }

    override fun onSignUp() {

    }
}