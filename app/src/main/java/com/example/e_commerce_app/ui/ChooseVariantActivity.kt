package com.example.e_commerce_app.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_commerce_app.Adapter.VariantAdapter
import com.example.e_commerce_app.databinding.ActivityChooseVariantBinding
import com.example.e_commerce_app.datastore.DataStoreManager
import com.example.e_commerce_app.datastore.DataStoreProvider
import com.example.e_commerce_app.model.Cart
import com.example.e_commerce_app.model.ProductX
import com.example.e_commerce_app.model.Variant
import com.example.e_commerce_app.util.RetrofitInstance
import com.example.e_commerce_app.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ChooseVariantActivity : AppCompatActivity() {
    lateinit var binding: ActivityChooseVariantBinding
    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var variantAdapter: VariantAdapter


    private val VariantOnClick by lazy {
        object : VariantAdapter.VariantOnClick {
            override fun onItemClick(variant: Variant, position: Int) {
                val NameVariant1 = variant.variant_name
                val PriceVariant = variant.variant_price
                val ImageVariant = variant.variant_image
                val ShopID = variant.Product.id_shop
                val variantID = variant.id
                val id_Product = variant.Product.id

                val prefix = ImageVariant?.let { Utils.extractPrefix(it) }

                binding.idVariant.text = variantID.toString()
                binding.idShop.text = ShopID.toString()
                binding.varianName.text = NameVariant1
                binding.variantPrice.text = PriceVariant.toString()
                binding.VariantImg.setImageBitmap(prefix?.let { Utils.decodeBase64ToBitmap(it) })
                binding.idProduct.text = id_Product.toString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseVariantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataStoreManager = DataStoreProvider.getInstance(this)
        getDetailProductRun()

        binding.CompleteChoose.setOnClickListener {
            AddCartRun()
        }
    }

    fun AddCartRun() {
        val intValue = intent.getIntExtra("quanlity", 0)

        val variant_id = binding.idVariant.text.toString().toInt()
        val id_Shop = binding.idShop.text.toString().toInt()
        val Price = binding.variantPrice.text.toString().toInt()
        val id_Product = binding.idProduct.text.toString().toInt()

        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreManager.getCurrentUser().collect {
                it.id?.let { it1 -> AddCart(it1, variant_id, id_Product, intValue, id_Shop, Price) }
            }
        }
    }

    fun AddCart(idUser: Int, variant_id: Int, maSP: Int, Quanlity: Int, id_Shop: Int, price: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val cart = Cart(
                    null.toString(),
                    null,
                    id_Shop,
                    maSP,
                    price,
                    null,
                    null,
                    Quanlity,
                    null,
                    idUser,
                    null,
                    variant_id
                )
                Log.d("DetailCart", "AddCart:${cart}")
                val response = RetrofitInstance.CartApi.AddProductCart(idUser.toString(), cart)
                if (response.isSuccessful && response.body() != null) {
                    if (response.body()!!.err.toString() == "0") {
                        withContext(Dispatchers.Main) {
                            val alertDialog = AlertDialog.Builder(this@ChooseVariantActivity)
                                .setTitle("Add Cart!")
                                .setMessage(
                                    "\n" +
                                            " You have add product to your cart!!"
                                )
                                .setPositiveButton("OK") { dialog, which ->
                                    // Handle positive button click
                                    dialog.dismiss()
                                    val intent = Intent(this@ChooseVariantActivity, CartActivity::class.java)
                                    startActivity(intent)
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
                            val alertDialog = AlertDialog.Builder(this@ChooseVariantActivity)
                                .setTitle("Add Cart!")
                                .setMessage(
                                    "\n" +
                                            "add product to your cart failed!!"
                                )
                                .setPositiveButton("OK") { dialog, which ->
                                    // Handle positive button click
                                    dialog.dismiss()
                                    val intent = Intent(this@ChooseVariantActivity, HomeActivity::class.java)
                                    startActivity(intent)
                                }
//                            .setNegativeButton("Cancel") { dialog, which ->
//                                // Handle negative button click
//                                dialog.dismiss()
//                            }
                                .create()

                            alertDialog.show()
                        }
                    }
                }
            } catch (e: HttpException) {
                Toast.makeText(this@ChooseVariantActivity, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(this@ChooseVariantActivity, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }
        }
    }


    private fun getDetailProductRun() {
        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreManager.getCurrentID().collect {
                variantAdapter = VariantAdapter(VariantOnClick)
                getVariant(it?.id.toString(), variantAdapter)
            }
        }
    }

    fun getVariant(id: String, variantAdapter: VariantAdapter) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.ProductApi.GetVariant(id)
                if (response.isSuccessful && response.body() != null) {
                    val listVariant = response.body()!!.variantData?.rows?.map {
                        val product = ProductX(
                            it.Product.category_id,
                            it.Product.createdAt,
                            it.Product.id,
                            it.Product.id_shop,
                            it.Product.product_decs,
                            it.Product.product_image,
                            it.Product.product_name,
                            it.Product.product_price,
                            it.Product.product_review,
                            it.Product.status,
                            it.Product.updatedAt
                        )
                        Variant(
                            product,
                            it.createdAt,
                            it.id,
                            it.id_product,
                            it.updatedAt,
                            it.variant_image,
                            it.variant_name,
                            it.variant_numbersell,
                            it.variant_price,
                            it.variant_selled
                        )
                    }

                    val NameVariant1 = response.body()!!.variantData?.rows?.get(0)?.variant_name
                    val PriceVariant = response.body()!!.variantData?.rows?.get(0)?.variant_price
                    val ImageVariant = response.body()!!.variantData?.rows?.get(0)?.variant_image
                    val ShopID = response.body()!!.variantData?.rows?.get(0)?.Product?.id_shop
                    val variantID = response.body()!!.variantData?.rows?.get(0)?.id
                    val id_Product = response.body()!!.variantData?.rows?.get(0)?.Product?.id

                    val prefix = ImageVariant?.let { Utils.extractPrefix(it) }
                    withContext(Dispatchers.Main) {
                        binding.idShop.text = ShopID.toString()
                        binding.varianName.text = NameVariant1
                        binding.variantPrice.text = PriceVariant.toString()
                        binding.idVariant.text = variantID.toString()
                        binding.idProduct.text = id_Product.toString()
                        Log.d("prefix", "getVariant: ${prefix}")
                        binding.VariantImg.setImageBitmap(prefix?.let { Utils.decodeBase64ToBitmap(it) })
                        variantAdapter.submitList(listVariant)
                        binding.rcvName.apply {
                            layoutManager = GridLayoutManager(this@ChooseVariantActivity, 2)
                            setHasFixedSize(true)
                            adapter = variantAdapter
                        }
                    }
                }
            } catch (e: HttpException) {
                Toast.makeText(this@ChooseVariantActivity, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(this@ChooseVariantActivity, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }
        }
    }
}