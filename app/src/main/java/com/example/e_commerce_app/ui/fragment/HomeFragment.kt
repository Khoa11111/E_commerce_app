package com.example.e_commerce_app.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce_app.Adapter.ProductAdapter
import com.example.e_commerce_app.R
import com.example.e_commerce_app.ViewModel.HomeViewModel
import com.example.e_commerce_app.databinding.FragmentHomeBinding
import com.example.e_commerce_app.databinding.FragmentSignupBinding
import com.example.e_commerce_app.model.Product
import com.example.e_commerce_app.util.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    lateinit var productList:ArrayList<Product>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.RcvHome.layoutManager=LinearLayoutManager(requireContext())
        binding.RcvHome.setHasFixedSize(true)
        getProductData()
        return binding.root
    }

    private fun getProductData(){
        productList = ArrayList<Product>()
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.UserApi.getAllProduct()
            }catch (e: HttpException){
                Toast.makeText(requireActivity(),"http error ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }catch (e: IOException){
                Toast.makeText(requireActivity(),"app error ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }


            if (response.isSuccessful && response.body() !=null) {
                val products = response.body()!!.productData?.rows
                withContext(Dispatchers.Main){
                    binding.apply {
                        products?.forEach { product ->
                            productList.add(product)
                            Log.d("ProductList", "getProductData:${productList.toString()} ")
                        }
                    }
                    binding.RcvHome.adapter=ProductAdapter(productList)
                }
                // Use the retrieved product data here
                // Update UI, store in database, etc.
            } else {
                // Handle API errors (e.g., non-zero error code)
                Log.e("Retrofit", "API error: ${response.message()}")
            }


        }
    }

}