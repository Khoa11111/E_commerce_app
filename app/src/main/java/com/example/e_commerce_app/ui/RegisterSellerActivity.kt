package com.example.e_commerce_app.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.e_commerce_app.MySharedPreferences.MySharedPreferences
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityLoginSignupBinding
import com.example.e_commerce_app.databinding.ActivityRegisterSellerBinding
import com.example.e_commerce_app.model.Shop
import com.example.e_commerce_app.model.User
import com.example.e_commerce_app.util.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.math.log

class RegisterSellerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterSellerBinding
    private var base64Strings: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val mySharedPreferences:MySharedPreferences(this)

        binding.imgvImgSeller.setOnClickListener{
            openImageChooser()
        }


        Log.d("Base641", "onCreate: ${base64Strings}")
    }

    private fun openImageChooser() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type= "image/*"
        startActivityForResult(intent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 ) {
            val imageUri = data?.data ?: return
            binding.imgvImgSeller.setImageURI(imageUri)
            val base64String = encodeImageToBase64(imageUri)
            Log.d("Base64", base64String?.length.toString())
        }
    }


    fun encodeImageToBase64(imageUri: Uri): String? {
        val inputStream = contentResolver.openInputStream(imageUri) ?: return null
        val byteArrayOutputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            byteArrayOutputStream.write(buffer, 0, length)
        }
        inputStream.close()
        val imageBytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }





}