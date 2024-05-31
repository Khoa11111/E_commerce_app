package com.example.e_commerce_app.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityShopProfileBinding
import com.example.e_commerce_app.util.RetrofitInstance
import com.example.e_commerce_app.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShopProfileActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityShopProfileBinding
    private var uri: Uri? = null
    private var base64String: String = ""

    private val imageContract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            uri = it
            binding.imgProfile.setImageURI(uri)
            base64String = Utils.encodeUriToBase64(uri!!, applicationContext)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnBackProfile.setOnClickListener(this@ShopProfileActivity)
            imgProfile.setOnClickListener(this@ShopProfileActivity)
            SubmitAdd.setOnClickListener(this@ShopProfileActivity)
        }


    }

    private fun submitInfo() {
        lifecycleScope.launch(Dispatchers.IO) {
//            val response = RetrofitInstance.ShopApi.updateInfoShop()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnBackProfile -> finish()
            binding.imgProfile -> imageContract.launch("image/*")
            binding.SubmitAdd -> submitInfo()
        }
    }
}