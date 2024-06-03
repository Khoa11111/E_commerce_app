package com.example.e_commerce_app.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_commerce_app.Adapter.ProductAdapter
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityProductByCategoryBinding
import com.example.e_commerce_app.datastore.DataStoreManager
import com.example.e_commerce_app.datastore.DataStoreProvider
import com.example.e_commerce_app.model.Category
import com.example.e_commerce_app.model.Product
import com.example.e_commerce_app.model.Shop
import com.example.e_commerce_app.util.RetrofitInstance
import com.example.e_commerce_app.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ProductByCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductByCategoryBinding
    private lateinit var productAdpter: ProductAdapter
    private lateinit var dataStoreManager: DataStoreManager

    private val productOnItemClick by lazy {
        object : ProductAdapter.ProductOnItemClick {
            override fun onItemClick(product: Product, position: Int) {
                lifecycleScope.launch(Dispatchers.IO) {
                    Log.d("onItemClick", "onItemClick:${product} ")
                    val product = Product(
                        product.id, product.product_name,
                        null.toString(), 0,
                        null.toString(), 0, 0, product.product_price,
                        null.toString(), null, null, null.toString(), null.toString(), null, null
                    )
                    dataStoreManager.storeCurrentID(product)
                    val intent = Intent(this@ProductByCategoryActivity, DetailPrActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductByCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreManager = DataStoreProvider.getInstance(this)

        setupProductRecycler()
    }

    private fun setupProductRecycler() {
        productAdpter = ProductAdapter(productOnItemClick)
        getListProduct(productAdpter)
    }

    private fun getListProduct(productAdapter: ProductAdapter) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                dataStoreManager.getIdCategory().collect {
                    val response = RetrofitInstance.ProductApi.getProductByCategory(it!!)
                    Log.d("getProductHome", response.body().toString())
                    if (response.isSuccessful && response.body() != null) {
                        if (response.body()!!.err.toString() == "0") {
                            val listProduct = response.body()!!.productData!!.rows?.map {
                                val category = it.category?.let { it1 ->
                                    Category(
                                        it1.id,
                                        it.category.category_name,
                                        it.category.category_image,
                                        it.category.createdAt,
                                        it.category.updatedAt
                                    )
                                }
                                val shop = it.shop?.let { it1 ->
                                    Shop(
                                        it1.id,
                                        it.shop.shop_name,
                                        it.shop.Image_shop,
                                        it.shop.Address,
                                        it.shop.id_user,
                                        it.shop.status,
                                        it.shop.createdAt,
                                        it.shop.createdAt
                                    )
                                }
                                Product(
                                    it.id,
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
                                    it.updatedAt,
                                    it.variant_name,
                                    it.numberSell
                                )
                            }

                            withContext(Dispatchers.Main) {
                                productAdapter.submitList(listProduct)
                                binding.RcvProductByCategory.apply {
                                    layoutManager = GridLayoutManager(this@ProductByCategoryActivity, 2)
                                    setHasFixedSize(true)
                                    adapter = productAdapter
                                }
                            }
                        }
                    }
                }
            } catch (e: HttpException) {
                Toast.makeText(this@ProductByCategoryActivity, "http error ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(this@ProductByCategoryActivity, "app error ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }
        }
    }
}