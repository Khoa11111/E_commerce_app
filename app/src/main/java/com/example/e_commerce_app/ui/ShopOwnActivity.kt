package com.example.e_commerce_app.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityProductShopBinding
import com.example.e_commerce_app.databinding.ActivityRegisterSellerBinding
import com.example.e_commerce_app.databinding.ActivityShopOwnBinding
import com.example.e_commerce_app.databinding.FragmentUserInfoBinding

class ShopOwnActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShopOwnBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopOwnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeseller.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }

        binding.Orderseller.setOnClickListener {

        }

        binding.ShopProfile.setOnClickListener {
            val intent = Intent(this, ShopProfileActivity::class.java)
            startActivity(intent)
        }

        binding.Myproductseller.setOnClickListener {
            val intent = Intent(applicationContext, ProductShopActivity::class.java)
            startActivity(intent)
        }

    }
}