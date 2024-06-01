package com.example.e_commerce_app.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce_app.databinding.ActivityShopProfileBinding
import com.example.e_commerce_app.datastore.DataStoreManager
import com.example.e_commerce_app.datastore.DataStoreProvider
import com.example.e_commerce_app.requestModel.UpdateInfoShopRequest
import com.example.e_commerce_app.util.RetrofitInstance
import com.example.e_commerce_app.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShopProfileActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityShopProfileBinding
    private lateinit var dataStoreManager: DataStoreManager
    private var uri: Uri? = null
    private var base64String: String? = null

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

        dataStoreManager = DataStoreProvider.getInstance(this)

        setValueInfo()

        binding.apply {
            btnBackProfile.setOnClickListener(this@ShopProfileActivity)
            imgProfile.setOnClickListener(this@ShopProfileActivity)
            SubmitAdd.setOnClickListener(this@ShopProfileActivity)
        }

    }

    private fun setValueInfo() {
        lifecycleScope.launch {
            dataStoreManager.getCurrentUser().collect {
                val response = RetrofitInstance.ShopApi.getShopByID(it.id)

                if (response.isSuccessful && response.body() != null) {
                    Log.d("setValueInfo", "setValueInfo:${response.body()} ")
                    val shopName = response.body()!!.userData.shop_name
                    val shopAddress = response.body()!!.userData.Address
                    val shopImage = response.body()!!.userData.Image_shop

                    val bitmap = Utils.decodeBase64ToBitmap(Utils.extractPrefix(shopImage))

                    withContext(Dispatchers.Main) {
                        binding.edtNameProfile.setText(shopName)
                        binding.prAddress.setText(shopAddress)
                            binding.imgProfile.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    private fun submitInfo() {
        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreManager.getCurrentUser().collect {
                it.shop?.let { it1 ->
                    val response = RetrofitInstance.ShopApi.updateInfoShop(
                        it1.id,
                        UpdateInfoShopRequest(
                            binding.edtNameProfile.text.toString().trim(),
                            binding.prAddress.text.toString().trim(),
                            base64String
                        )
                    )

                    if (response.isSuccessful && response.body() != null) {
                        withContext(Dispatchers.Main) {
                            if (response.body()!!.err.toString() == "1") {
                                Toast.makeText(
                                    this@ShopProfileActivity,
                                    "Update fail! Please try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(this@ShopProfileActivity, "Update success!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
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