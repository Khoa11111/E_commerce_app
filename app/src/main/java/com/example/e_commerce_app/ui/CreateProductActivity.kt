package com.example.e_commerce_app.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce_app.Adapter.CategoryAdapterSpinner
import com.example.e_commerce_app.databinding.ActivityCreateProductBinding
import com.example.e_commerce_app.datastore.DataStoreManager
import com.example.e_commerce_app.datastore.DataStoreProvider
import com.example.e_commerce_app.model.Category
import com.example.e_commerce_app.model.Product
import com.example.e_commerce_app.util.RetrofitInstance
import com.example.e_commerce_app.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class CreateProductActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateProductBinding
    lateinit var categoryAdapterSpinner: CategoryAdapterSpinner
    lateinit var dataStoreManager: DataStoreManager
    private var uri: Uri? = null
    private var base64String: String = ""
    private val categoryOnItemClick by lazy {
        object : CategoryAdapterSpinner.CategoryOnClick {
            override fun onItemClick(category: Category, position: Int) {
                lifecycleScope.launch {
                    dataStoreManager.storeCurrentIDCategory(category)
                    Log.d("categorySSSS", "onItemClick: ${category}")
                }
//                Toast.makeText(this@CreateProductActivity, category.category_name, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private val imageContract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            uri = it
            binding.AddImageProductEdt.setImageURI(uri)
            base64String = Utils.encodeUriToBase64(uri!!, applicationContext)
            Log.d("check", base64String.length.toString())
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataStoreManager = DataStoreProvider.getInstance(this)
        setupCategorySpinner()

        binding.AddImageProductEdt.setOnClickListener{
            imageContract.launch("image/*")
        }

        binding.SubmitAdd.setOnClickListener{
            CreateProductgetData()
        }
        binding.CancleBtn.setOnClickListener{
            finish()
        }
    }

    private fun CreateProductgetData(){
        val NameProduct= binding.AddNameProductEdt.text.toString()
        val Price=binding.AddPriceProductEdt.text.toString().toInt()
        val NumberSell = binding.NumberSell.text.toString().toInt()
        val variant_name = binding.AddNameVariantEdt.text.toString()

        val Decs =binding.AddDecsProductEdt.text.toString()
        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreManager.getCurrentIDCategory().collect{
                Log.d("CreateProductgetData", "CreateProductgetData:${it?.id} ")
                lifecycleScope.launch(Dispatchers.IO) {
                    dataStoreManager.getCurrentUser().collect{
                        it.shop?.let { it1 ->
                            it?.id?.let { it2 ->
                                CreateProduct(NameProduct,Price,Decs,
                                    it1.id, it2,variant_name,NumberSell)
                            }
                        }
                    }
                }
            }
        }
    }



    private fun CreateProduct(Name:String,Price:Int,Decs:String,id_Shop:Int,category_id:Int,variant_name:String,Numbersell:Int){
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val product=Product(null,Name,Decs,category_id,null,id_Shop,null,Price,base64String,null,null,null,null,variant_name,Numbersell)
                Log.d("checkProduct", "CreateProduct: ${product}")
                val response = RetrofitInstance.ProductApi.CreateProduct(product)
                if (response.isSuccessful && response.body() != null) {
                    if (response.body()!!.err.toString() == "1") {
                        withContext(Dispatchers.Main) {
                            val alertDialog = AlertDialog.Builder(this@CreateProductActivity)
                                .setTitle("Add Product!")
                                .setMessage(
                                    "\n" +
                                            "Add Product failed!!!"
                                )
                                .setPositiveButton("OK") { dialog, which ->
                                    // Handle positive button click
                                    dialog.dismiss()
                                }
//                            .setNegativeButton("Cancel") { dialog, which ->
//                                // Handle negative button click
//                                dialog.dismiss()
//                            }
                                .create()
                            alertDialog.show()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            val alertDialog = AlertDialog.Builder(this@CreateProductActivity)
                                .setTitle("Add Product!")
                                .setMessage(
                                    "\n" +
                                            "Add Product failed!!!"
                                )
                                .setPositiveButton("OK") { dialog, which ->
                                    // Handle positive button click
                                    dialog.dismiss()
                                }
//                            .setNegativeButton("Cancel") { dialog, which ->
//                                // Handle negative button click
//                                dialog.dismiss()
//                            }
                                .create()
                            alertDialog.show()
                            val intent = Intent(applicationContext, ProductShopActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    // This block executes only if the response is successful and has a body
                }
            }catch (e: HttpException) {
                Toast.makeText(applicationContext, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(applicationContext, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }
        }
    }


    private fun setupCategorySpinner() {
        categoryAdapterSpinner = CategoryAdapterSpinner(this, categoryOnItemClick)
        binding.spCategory.adapter = categoryAdapterSpinner
        getCategorySpiner()
    }

    private fun getCategorySpiner() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val responseCategory = RetrofitInstance.CategoryApi.getAllCategory()
                if (responseCategory.isSuccessful && responseCategory.body() != null) {
                    if (responseCategory.body()!!.err.toString() == "0") {
                        val listCategory = responseCategory.body()!!.categoryData!!.rows.map {
                            Category(it.id, it.category_name, it.category_image, it.createdAt, it.updatedAt)
                        }
                        withContext(Dispatchers.Main) {
                            categoryAdapterSpinner.addAll(listCategory)
                        }
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CreateProductActivity, "HTTP error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@CreateProductActivity, "App error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}