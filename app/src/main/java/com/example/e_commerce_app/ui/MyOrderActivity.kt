package com.example.e_commerce_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityMyOrderBinding

class MyOrderActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMyOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ActionEvent()
    }

    fun ActionEvent(){
        binding.AllMyorder.setOnClickListener{

        }
        binding.WaitMyorder.setOnClickListener{

        }
        binding.ApproveMyorder.setOnClickListener{

        }
        binding.CancelMyorder.setOnClickListener{

        }
        binding.CompleteMyorder.setOnClickListener{

        }
    }



}