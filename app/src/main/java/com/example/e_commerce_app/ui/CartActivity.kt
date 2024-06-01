package com.example.e_commerce_app.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_commerce_app.Adapter.CartAdapter
import com.example.e_commerce_app.Adapter.VariantAdapter
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityCartBinding
import com.example.e_commerce_app.datastore.DataStoreManager
import com.example.e_commerce_app.datastore.DataStoreProvider
import com.example.e_commerce_app.model.*
import com.example.e_commerce_app.util.RetrofitInstance
import com.example.e_commerce_app.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class CartActivity : AppCompatActivity() {
    lateinit var binding:ActivityCartBinding
    lateinit var cartAdapter: CartAdapter
    lateinit var dataStoreManager: DataStoreManager

    private val ProductCartonClick by lazy {
        object : CartAdapter.ProductCartonClick {
            override fun onItemClick(cart: Cart, position: Int) {
                lifecycleScope.launch(Dispatchers.IO) {
                    dataStoreManager.getCurrentUser().collect{
                        try {
                            val response= RetrofitInstance.CartApi.DeleteCart(it.id.toString(),
                                cart.variant_id.toString()
                            )
                            if (response.body()!!.err.toString() == "0") {
                                withContext(Dispatchers.Main) {
                                    val alertDialog = AlertDialog.Builder(this@CartActivity)
                                        .setTitle("Delete Cart!")
                                        .setMessage(
                                            "\n" +
                                                    "Delete product success!!"
                                        )
                                        .setPositiveButton("OK") { dialog, which ->
                                            // Handle positive button click
                                            dialog.dismiss()
                                            val intent = Intent(applicationContext, RGSellerOtpActivity::class.java)
                                            startActivity(intent)
                                        }
                                        .create()
                                    alertDialog.show()
                                }
                            }else{
                                withContext(Dispatchers.Main) {
                                    val alertDialog = AlertDialog.Builder(this@CartActivity)
                                        .setTitle("Delete Cart!")
                                        .setMessage(
                                            "\n" +
                                                    "Delete product failed!!"
                                        )
                                        .setPositiveButton("OK") { dialog, which ->
                                            dialog.dismiss()
                                            val intent = Intent(applicationContext, RGSellerOtpActivity::class.java)
                                            startActivity(intent)
                                        }
                                        .create()
                                    alertDialog.show()
                                }
                            }
                        }catch (e: HttpException) {
                            Toast.makeText(this@CartActivity, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                        } catch (e: IOException) {
                            Toast.makeText(this@CartActivity, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }

                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataStoreManager = DataStoreProvider.getInstance(this)
        getAllCartRun()

        binding.btnBackMain.setOnClickListener{
            finish()
        }
    }


    fun getAllCartRun(){
        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreManager.getCurrentUser().collect{
                cartAdapter=CartAdapter(ProductCartonClick)
                Log.d("id_userssss", "getAllCartRun: "+it.id.toString())
                it.id?.let { it1 -> getAllCart(it1,cartAdapter) }
            }
        }
    }
    fun getAllCart(id:Int,cartAdapter: CartAdapter){
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.CartApi.GetCart(id)
                if (response.isSuccessful && response.body() != null) {
                    Log.d("getAllCart", "getAllCart: ${response.body()}")
                    if (response.body()!!.err.toString() == "0") {
                        val listCart = response.body()!!.dataCart?.rows?.map {
                            val shop= it.shop?.let { it1 ->
                                Shop(
                                    it.shop.id,
                                    it.shop.shop_name,
                                    it.shop.Image_shop,
                                    it.shop.Address,
                                    it.shop.id_user,
                                    it.shop.status,
                                    it.shop.createdAt,
                                    it.shop.updatedAt
                                )
                            }
                            val productCartData = it.productCartData?.let { it1 ->
                                ProductCartData(
                                    it.productCartData.category_id,
                                    it.productCartData.createdAt,
                                    it1.id,
                                    it.productCartData.id_shop,
                                    it.productCartData.product_decs,
                                    it.productCartData.product_image,
                                    it.productCartData.product_name,
                                    it.productCartData.product_price,
                                    it.productCartData.product_review,
                                    it.productCartData.status,
                                    it.productCartData.updatedAt
                                )
                            }
                            Cart(
                                it.createdAt,
                                it.id,
                                it.id_shop,
                                it.maSP,
                                it.price,
                                productCartData,
                                shop,
                                it.soLuongMua,
                                it.status,
                                it.uid,
                                it.updatedAt,
                                it.variant_id
                            )
                        }

                        withContext(Dispatchers.Main) {
                            cartAdapter.submitList(listCart)
                            binding.rvCart.apply {
                                layoutManager = GridLayoutManager(this@CartActivity, 1)
                                setHasFixedSize(true)
                                adapter = cartAdapter
                            }
                        }
                    }
                }
            }catch (e: HttpException) {
                Toast.makeText(this@CartActivity, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(this@CartActivity, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }
        }
    }

}