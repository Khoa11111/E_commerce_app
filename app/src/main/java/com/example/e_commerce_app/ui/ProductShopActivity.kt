package com.example.e_commerce_app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityProductShopBinding
import com.example.e_commerce_app.databinding.ActivityRegisterSellerBinding
import com.example.e_commerce_app.util.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ProductShopActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductShopBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.Addproduct.setOnClickListener{

        }

        binding.BtnBackProduct.setOnClickListener{

        }

        binding.BtnRefreshSeller.setOnClickListener{

        }

        binding.BtnHomeSeller.setOnClickListener{

        }


    }
    fun getProductShop(id:String){
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.ProductApi.GetProduct_ID(id)
            }catch (e: HttpException) {
                Toast.makeText(this@ProductShopActivity, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(this@ProductShopActivity, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {

                // This block executes only if the response is successful and has a body
            }
        }
    }

}