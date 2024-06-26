package com.example.e_commerce_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityLoginSignupBinding
import com.example.e_commerce_app.model.User
import com.example.e_commerce_app.util.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

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
        navController = navHostFragment.findNavController()
        navController.setGraph(R.navigation.nav_graph_login_signup)
    }

}