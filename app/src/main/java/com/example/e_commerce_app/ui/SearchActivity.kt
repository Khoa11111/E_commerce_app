package com.example.e_commerce_app.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce_app.Adapter.ProductSearchAdapter
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivitySearchBinding
import com.example.e_commerce_app.datastore.DataStoreManager
import com.example.e_commerce_app.datastore.DataStoreProvider
import com.example.e_commerce_app.model.Product
import com.example.e_commerce_app.util.RetrofitInstance
import com.example.e_commerce_app.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreManager = DataStoreProvider.getInstance(this)

        setUpAutoCompleteSearch()
        focusAutoSearch()
        ClickSearch()


    }

    private fun ClickSearch() {
        binding.autoCompleteTextView.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Perform your desired action here
                hideKeyboard(binding.autoCompleteTextView)
                true
            } else {
                false
            }
        }
    }

    // Open keyboard
    private fun openKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    // Hide keyboard
    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun focusAutoSearch() {
        if (!binding.autoCompleteTextView.isFocused) {
            binding.autoCompleteTextView.requestFocus()
            openKeyboard()
        }
    }

    private fun setUpAutoCompleteSearch() {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = RetrofitInstance.ProductApi.getAllProduct("1")

            if (response.isSuccessful && response.body() != null) {
                if (response.body()!!.err.toString() == "0") {
                    val listNameProduct = response.body()!!.productData!!.rows!!.map {
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
                            null,
                            null,
                            it.createdAt,
                            it.updatedAt,
                            it.variant_name,
                            it.numberSell
                        )
                    }
                    withContext(Dispatchers.Main) {
                        val adapter = ProductSearchAdapter(this@SearchActivity, listNameProduct)
                        binding.autoCompleteTextView.threshold = 1
                        binding.autoCompleteTextView.setAdapter(adapter)
                        binding.autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
                            val selectedItem = adapter.getItem(position) as Product
                            binding.autoCompleteTextView.setText(selectedItem.product_name)
                            binding.autoCompleteTextView.clearFocus()
                            hideKeyboard(binding.autoCompleteTextView)
                            lifecycleScope.launch(Dispatchers.IO) {
                                val product = Product(
                                    selectedItem.id, selectedItem.product_name,
                                    null.toString(), 0,
                                    null.toString(), 0, 0, 0,
                                    null.toString(), null, null, null.toString(), null.toString(), null, null
                                )
                                dataStoreManager.storeCurrentID(product)
                            }
                            val intent = Intent(this@SearchActivity, DetailPrActivity::class.java)
                            startActivity(intent)
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SearchActivity, "Response Error", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SearchActivity, "Retrofit Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}