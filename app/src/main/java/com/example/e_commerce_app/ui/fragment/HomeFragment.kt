package com.example.e_commerce_app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_commerce_app.Adapter.ProductAdapter
import com.example.e_commerce_app.databinding.FragmentHomeBinding
import com.example.e_commerce_app.datastore.DataStoreManager
import com.example.e_commerce_app.datastore.DataStoreProvider
import com.example.e_commerce_app.model.Category
import com.example.e_commerce_app.model.Product
import com.example.e_commerce_app.model.Shop
import com.example.e_commerce_app.ui.DetailPrActivity
import com.example.e_commerce_app.util.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var productAdpter: ProductAdapter
    private lateinit var dataStoreManager: DataStoreManager

    private val productOnItemClick by lazy {
        object : ProductAdapter.ProductOnItemClick {
            override fun onItemClick(product: Product, position: Int) {
                lifecycleScope.launch(Dispatchers.IO) {
                    Log.d("onItemClick", "onItemClick:${product} ")
                    val product = Product(product.id,product.product_name,
                        null.toString(),0,
                        null.toString(),0,0, product.product_price,
                        null.toString(),null,null, null.toString(), null.toString(),null,null
                    )
                    dataStoreManager.storeCurrentID(product)
                    val intent = Intent(context, DetailPrActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupProductRecycler()

        dataStoreManager = DataStoreProvider.getInstance(requireContext())

        binding.CartBtn.setOnClickListener{
            val intent = Intent(requireContext(), CartActivity::class.java)
            startActivity(intent)
        }

        gotoSearch()

        return binding.root
    }

    private fun gotoSearch() {
        binding.search.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupProductRecycler() {
        productAdpter = ProductAdapter(productOnItemClick)
        getListProduct(productAdpter)
    }

    private fun getListProduct(productAdapter: ProductAdapter) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.ProductApi.getAllProduct("1")
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
                            binding.RcvHome.apply {
                                layoutManager = GridLayoutManager(requireContext(), 2)
                                setHasFixedSize(true)
                                adapter = productAdapter
                            }
                        }
                    }
                }
            } catch (e: HttpException) {
                Toast.makeText(requireActivity(), "http error ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(requireActivity(), "app error ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }
        }
    }
}