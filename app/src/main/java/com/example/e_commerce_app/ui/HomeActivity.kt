package com.example.e_commerce_app.ui

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navcontroller: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavController()

        onSelectedItemNav()
    }

    private fun onSelectedItemNav() {
        binding.bottomNavigationView.setupWithNavController(navcontroller)
    }

    private fun setUpNavController() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragmentHome) as NavHostFragment
        navcontroller = navHostFragment.findNavController()
        navcontroller.setGraph(R.navigation.nav_graph_home)
    }
}