package com.example.e_commerce_app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce_app.Adapter.CategoryAdapter
import com.example.e_commerce_app.Adapter.CategoryAdapterSpinner
import com.example.e_commerce_app.databinding.ActivityCreateProductBinding
import com.example.e_commerce_app.model.Category
import com.example.e_commerce_app.util.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class CreateProductActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateProductBinding
    lateinit var categoryAdapterSpinner: CategoryAdapterSpinner
    private val categoryOnItemClick by lazy {
        object : CategoryAdapter.CategoryOnItemClick {
            override fun onItemClick(category: Category, position: Int) {
                Toast.makeText(this@CreateProductActivity, category.category_name, Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

//    private fun setupCategorySpinner() {
//        categoryAdapterSpinner = categoryAdapterSpinner(categoryOnItemClick)
//        getListCategory(categoryAdapter)
//    }

    private fun getCategorySpiner(categorySpinnerAdapter: CategoryAdapterSpinner){
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val responseCategory = RetrofitInstance.CategoryApi.getAllCategory()

                if (responseCategory.isSuccessful && responseCategory.body() != null) {
                    if (responseCategory.body()!!.err.toString() == "0") {
                        val listCategory = responseCategory.body()!!.categoryData!!.rows.map {
                            Category(it.id, it.category_name, it.category_image, it.createdAt, it.updatedAt)
                        }

                        withContext(Dispatchers.Main) {
                            categoryAdapterSpinner.submitList(listCategory)
                            binding.spCategory.apply {

                            }
                        }


                    }

                }


            }catch (e: HttpException) {
                Toast.makeText(this@CreateProductActivity, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(this@CreateProductActivity, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }
        }
    }
}