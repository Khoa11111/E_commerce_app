package com.example.e_commerce_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_commerce_app.Adapter.CartAdapter
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityCheckOutBinding
import com.example.e_commerce_app.datastore.DataStoreManager
import com.example.e_commerce_app.datastore.DataStoreProvider
import com.example.e_commerce_app.model.Cart
import com.example.e_commerce_app.model.Product
import com.example.e_commerce_app.model.ProductCartData
import com.example.e_commerce_app.model.Shop
import com.example.e_commerce_app.requestModel.ItemRequest
import com.example.e_commerce_app.requestModel.OrderRequest
import com.example.e_commerce_app.util.RetrofitInstance
import com.example.e_commerce_app.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class CheckOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckOutBinding
    lateinit var cartAdapter: CartAdapter
    lateinit var dataStoreManager: DataStoreManager

    private val ProductCartonClick by lazy {
        object : CartAdapter.ProductCartonClick {
            override fun onItemClick(view: View, cart: Cart, position: Int) {

            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataStoreManager = DataStoreProvider.getInstance(this)

        getAllCartRun()
        Order()
    }

    private fun groupProductsByShop(listProducts: List<Cart>): Map<Int, List<Cart>> {
        return listProducts.groupBy { it.id_shop!! }
    }


    //
    // Access products for a specific shop


    private fun Order() {
        binding.btnOrderComplete.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val cartAdapter: CartAdapter = binding.rcvOrder.adapter as CartAdapter
                val cartList = cartAdapter.currentList
                if (cartList.size != 0) {
                    val groupedProducts = groupProductsByShop(cartList)
                    for (shopId in groupedProducts.keys) {
                        val shopProducts = groupedProducts[shopId]!!
                        for (cart in shopProducts) {
                            Log.d(
                                Utils.TAG,
                                "shopProducts: quantity = ${cart.soLuongMua} : id variant: ${cart.variant_id}"
                            )
                        }
                    }
                }
            }
        }
    }

    fun getAllCartRun() {
        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreManager.getCurrentUser().collect {
                cartAdapter = CartAdapter(ProductCartonClick)
                Log.d("id_userssss", "getAllCartRun: " + it.id.toString())
                it.id?.let { it1 -> getAllCart(it1, cartAdapter) }
            }
        }
    }

    fun getAllCart(id: Int, cartAdapter: CartAdapter) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.CartApi.GetCart(id)
                if (response.isSuccessful && response.body() != null) {
                    Log.d("getAllCart", "getAllCart: ${response.body()}")
                    if (response.body()!!.err.toString() == "0") {
                        val listCart = response.body()!!.dataCart?.rows?.map {
                            val shop = it.shop?.let { it1 ->
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
                            binding.rcvOrder.apply {
                                layoutManager = GridLayoutManager(this@CheckOutActivity, 1)
                                setHasFixedSize(true)
                                adapter = cartAdapter
                            }
                        }
                    }
                }
            } catch (e: HttpException) {
                Toast.makeText(this@CheckOutActivity, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(this@CheckOutActivity, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }
        }
    }
}