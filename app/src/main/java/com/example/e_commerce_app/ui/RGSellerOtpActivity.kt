package com.example.e_commerce_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityRgsellerOtpBinding

class RGSellerOtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRgsellerOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRgsellerOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTest.setOnClickListener {
            
        }
    }
}