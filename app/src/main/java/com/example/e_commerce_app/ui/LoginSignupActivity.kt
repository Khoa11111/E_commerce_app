package com.example.e_commerce_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityLoginSignupBinding

class LoginSignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginSignupBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavController()
    }

    private fun setUpNavController() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragmentLoginSignup) as NavHostFragment
        navController = navHostFragment.navController
    }
}