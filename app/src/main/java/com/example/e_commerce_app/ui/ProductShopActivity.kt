package com.example.e_commerce_app.ui

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_commerce_app.Adapter.CategoryAdapterSpinner
import com.example.e_commerce_app.Adapter.ProductShopAdapter
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityProductShopBinding
import com.example.e_commerce_app.model.Category
import com.example.e_commerce_app.model.Product
import com.example.e_commerce_app.model.Shop
import com.example.e_commerce_app.util.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ProductShopActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductShopBinding
    private lateinit var productShopAdapter: ProductShopAdapter
//    private val listCategory =
    private val productOnItemClick by lazy {
        object : ProductShopAdapter.ProductOnItemClick {
            override fun onItemClick(product: Product, position: Int) {
                Toast.makeText(this@ProductShopActivity, product.product_name, Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductShopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupProductRecyclerShop()

        binding.Addproduct.setOnClickListener{

        }

        binding.BtnBackProduct.setOnClickListener{

        }

        binding.BtnRefreshSeller.setOnClickListener{

        }

        binding.BtnHomeSeller.setOnClickListener{

        }


    }



    private fun setupProductRecyclerShop() {
        productShopAdapter = ProductShopAdapter(productOnItemClick)
        getProductShop(productShopAdapter,"3")
    }

    fun getProductShop(productShopAdapter: ProductShopAdapter, id: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.ProductApi.GetProduct_ID(id)

                Log.d("getProductShop", response.body().toString())

                if (response.isSuccessful && response.body() != null) {
                    if (response.body()!!.err.toString() == "0") {
                        val listProduct = response.body()!!.productData!!.rows.map {
                            val category = Category(
                                it.category.id,
                                it.category.category_name,
                                it.category.category_image,
                                it.category.createdAt,
                                it.category.updatedAt
                            )
                            val shop = Shop(
                                it.shop.id,
                                it.shop.shop_name,
                                it.shop.Image_shop,
                                it.shop.Address,
                                it.shop.id_user,
                                it.shop.status,
                                it.shop.createdAt,
                                it.shop.createdAt
                            )
                            Product(
                                it.category_id,
                                it.product_name,
                                it.product_decs,
                                it.category_id,
                                it.status,
                                it.id_shop,
                                it.product_review,
                                it.product_price,
                                it.product_image,
                                shop,
                                category,
                                it.createdAt,
                                it.updatedAt
                            )
                        }

                        withContext(Dispatchers.Main) {
                            productShopAdapter.submitList(listProduct)
                            binding.productShopRcv.apply {
                                layoutManager = GridLayoutManager(this@ProductShopActivity, 1)
                                setHasFixedSize(true)
                                adapter = productShopAdapter
                            }
                        }
                    }

                }
            } catch (e: HttpException) {
                Toast.makeText(this@ProductShopActivity, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(this@ProductShopActivity, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }
        }
    }

}