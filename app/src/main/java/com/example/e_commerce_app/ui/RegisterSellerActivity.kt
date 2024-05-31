package com.example.e_commerce_app.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce_app.databinding.ActivityRegisterSellerBinding
import com.example.e_commerce_app.datastore.DataStoreManager
import com.example.e_commerce_app.datastore.DataStoreProvider
import com.example.e_commerce_app.model.RegistorSellerData
import com.example.e_commerce_app.util.RetrofitInstance
import com.example.e_commerce_app.util.Utils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.shareIn
import retrofit2.HttpException
import java.io.IOException

class RegisterSellerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterSellerBinding
    private lateinit var dataStoreManager: DataStoreManager
    private var uri: Uri? = null
    private var base64String: String = ""

    private val imageContract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            uri = it
            binding.imgvImgSeller.setImageURI(uri)
            base64String = Utils.encodeUriToBase64(uri!!, applicationContext)
            Log.d("check", base64String.length.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreManager = DataStoreProvider.getInstance(this)

        binding.imgvImgSeller.setOnClickListener {
            imageContract.launch("image/*")
        }
        binding.btnSignupSeller.setOnClickListener {
            registerSellerRun(this)
//            Toast.makeText(this@RegisterSellerActivity,"registerSellerRun ",Toast.LENGTH_LONG).show()
        }
        binding.BtnBack.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerSellerRun(context: Context) {
        var email: String? = null
        var id: Int? = null

        dataStoreManager.idCurrenUserFlow.asLiveData().observe(this,{
            id=it
        })
        Toast.makeText(this@RegisterSellerActivity,"registerSellerRun${id} ",Toast.LENGTH_LONG).show()

//        lifecycleScope.launch {
//
////            dataStoreManager.idCurrenUserFlow.collect{
////                id
////            }
////            dataStoreManager.emailCurrentUserFlow.collect{
////                email
////            }
////            registerSeller(
////                binding.edtShopName.text.toString(),
////                binding.edtAddressSeller.text.toString(),
////                id!!,
////                email!!,
////                binding.edtReasonSeller.text.toString(),
////                base64String
////            )
//        }
    }



    private fun registerSeller(
        shop_name: String,
        Address: String,
        id_user: Int,
        email_user: String,
        reason: String,
        image_shop: String
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = try {
                var email: String? = null
                var id: Int? = null
                dataStoreManager.idCurrenUserFlow.collect{
                    id
                }
                dataStoreManager.emailCurrentUserFlow.collect{
                    email
                }
                val registorSellerData = RegistorSellerData(
                    null,
                    shop_name,
                    Address,
                    id_user.toString(),
                    reason,
                    email_user,
                    image_shop,
                    null,
                    null
                )

                RetrofitInstance.UserApi.RegSeller(registorSellerData)
            } catch (e: HttpException) {
                Toast.makeText(applicationContext, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(applicationContext, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                if (response.body()!!.err.toString() == "1") {
                    withContext(Dispatchers.Main) {
                        val alertDialog = AlertDialog.Builder(this@RegisterSellerActivity)
                            .setTitle("Register Fail!")
                            .setMessage(
                                "\n" +
                                        "You have already registered, please wait for approval from admin"
                            )
                            .setPositiveButton("OK") { dialog, which ->
                                // Handle positive button click
                                dialog.dismiss()
                            }
//                            .setNegativeButton("Cancel") { dialog, which ->
//                                // Handle negative button click
//                                dialog.dismiss()
//                            }
                            .create()
                        alertDialog.show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        val intent = Intent(applicationContext, RGSellerOtpActivity::class.java)
                        startActivity(intent)
                    }
                }
                // This block executes only if the response is successful and has a body
            }
        }
    }


}