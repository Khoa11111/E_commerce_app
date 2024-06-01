package com.example.e_commerce_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_commerce_app.Adapter.CartAdapter
import com.example.e_commerce_app.Adapter.VariantAdapter
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityCartBinding
import com.example.e_commerce_app.datastore.DataStoreManager
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

            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getAllCartRun()
    }


    fun getAllCartRun(){
        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreManager.getCurrentUser().collect{
                getAllCart(it.id.toString(),cartAdapter)
            }
        }
    }
    fun getAllCart(id:String,cartAdapter: CartAdapter){
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.CartApi.GetCart(id)
                if (response.isSuccessful && response.body() != null) {
                    if (response.body()!!.err.toString() == "0") {
                        val listCart = response.body()!!.cartData?.rows?.map {
                            val shop= it.shop?.let { it1 ->
                                Shop(
                                    it1.id,
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
                                layoutManager = GridLayoutManager(this@CartActivity, 2)
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