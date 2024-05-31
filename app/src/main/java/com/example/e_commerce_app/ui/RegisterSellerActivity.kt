package com.example.e_commerce_app.ui


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import com.example.e_commerce_app.MySharedPreferences.MySharedPreferences
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityLoginSignupBinding
import com.example.e_commerce_app.databinding.ActivityRegisterSellerBinding
import com.example.e_commerce_app.model.RegistorSellerData
import com.example.e_commerce_app.model.Shop
import com.example.e_commerce_app.model.User
import com.example.e_commerce_app.util.RetrofitInstance
import com.example.e_commerce_app.util.Utils
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

//        val mySharedPreferences:MySharedPreferences(this)

        binding.imgvImgSeller.setOnClickListener{
            imageContract.launch("image/*")
        }
        binding.btnSignupSeller.setOnClickListener{
            registerSeller(binding.edtShopName.text.toString(),binding.edtAddressSeller.text.toString(),2,"vanphattk159@gmail.com",binding.edtReasonSeller.text.toString(),base64String)
        }
        binding.BtnBack.setOnClickListener{
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }
//        Log.d("Base641", "onCreate: ${base64Strings}")
    }


    fun registerSeller(shop_name: String, Address: String, id_user: Int, email_user: String,reason:String,image_shop: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                val registorSellerData = RegistorSellerData(null, shop_name, Address, id_user.toString(), reason, email_user,image_shop, null, null)

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
                    val alertDialog = AlertDialog.Builder(this@RegisterSellerActivity)
                        .setTitle("Register Fail!")
                        .setMessage("\n" +
                                "You have already registered, please wait for approval from admin")
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
                }else{
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