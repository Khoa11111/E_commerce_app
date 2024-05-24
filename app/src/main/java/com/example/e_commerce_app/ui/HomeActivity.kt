package com.example.e_commerce_app.ui

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityHomeBinding
import java.io.ByteArrayOutputStream

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavController()
        onSelectedItemNav()
    }

    private fun onSelectedItemNav() {
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun setUpNavController() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragmentHome) as NavHostFragment
        navController = navHostFragment.navController
    }
}