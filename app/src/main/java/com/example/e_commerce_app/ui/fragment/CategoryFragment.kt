package com.example.e_commerce_app.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_commerce_app.Adapter.CategoryAdapter
import com.example.e_commerce_app.databinding.FragmentCategoryBinding
import com.example.e_commerce_app.datastore.DataStoreManager
import com.example.e_commerce_app.datastore.DataStoreProvider
import com.example.e_commerce_app.model.Category
import com.example.e_commerce_app.ui.ProductByCategoryActivity
import com.example.e_commerce_app.util.RetrofitInstance
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var dataStoreManager: DataStoreManager
    private val categoryOnItemClick by lazy {
        object : CategoryAdapter.CategoryOnItemClick {
            override fun onItemClick(category: Category, position: Int) {
                lifecycleScope.launch {
                    dataStoreManager.storeIdCategory(category.id)
                }
                val intent = Intent(requireContext(), ProductByCategoryActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        dataStoreManager = DataStoreProvider.getInstance(requireContext())

        setupCategoryRecycler()

        return binding.root
    }

    private fun setupCategoryRecycler() {
        categoryAdapter = CategoryAdapter(categoryOnItemClick)
        getListCategory(categoryAdapter)
    }

    private fun getListCategory(categoryAdapter: CategoryAdapter) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.CategoryApi.getAllCategory()

                if (response.isSuccessful && response.body() != null) {
                    if (response.body()!!.err.toString() == "0") {
                        val listCategory = response.body()!!.categoryData!!.rows.map {
                            Category(it.id, it.category_name, it.category_image, it.createdAt, it.updatedAt)
                        }

                        withContext(Dispatchers.Main) {
                            categoryAdapter.submitList(listCategory)
                            binding.recylerCategory.apply {
                                layoutManager = GridLayoutManager(requireContext(), 2)
                                setHasFixedSize(true)
                                adapter = categoryAdapter
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