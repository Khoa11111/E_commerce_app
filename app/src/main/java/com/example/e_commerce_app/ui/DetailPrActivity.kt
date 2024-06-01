package com.example.e_commerce_app.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_commerce_app.Adapter.ProductAdapter
import com.example.e_commerce_app.ChooseVariantActivity
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityDetailPrBinding
import com.example.e_commerce_app.databinding.ActivityProfileBinding
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

class DetailPrActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPrBinding
    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var productAdpter: ProductAdapter

    private val productOnItemClick by lazy {
        object : ProductAdapter.ProductOnItemClick {
            override fun onItemClick(product: Product, position: Int) {
                lifecycleScope.launch(Dispatchers.IO) {
                    Log.d("onItemClick", "onItemClick:${product} ")
                    val product = Product(product.id,product.product_name,
                        null.toString(),0,
                        null.toString(),0,0,product.product_price,
                        null.toString(),null,null, null.toString(), null.toString(),null,null
                    )
                    dataStoreManager.storeCurrentID(product)
                    val intent = Intent(this@DetailPrActivity, DetailPrActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailPrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreManager = DataStoreProvider.getInstance(this)

        binding.add1.setOnClickListener{
            val quanlity=binding.edtSlgCart.text.toString()
            val intQuan:Int=quanlity.toInt()
            val changeQuan=intQuan+1
            binding.edtSlgCart.setText(changeQuan.toString())
        }
        binding.remove1.setOnClickListener{
            val quanlity=binding.edtSlgCart.text.toString()
            val intQuan:Int=quanlity.toInt()
            val changeQuan=intQuan-1
            binding.edtSlgCart.setText(changeQuan.toString())
        }
        binding.imageButtonback.setOnClickListener{
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }

        binding.btnaddCart.setOnClickListener{
            val intent = Intent(applicationContext, ChooseVariantActivity::class.java)
            intent.putExtra("quanlity",binding.edtSlgCart.text.toString().toInt())
            startActivity(intent)
        }

        getDetailProductRun()
        setupProductRecycler()

    }




    private fun getDetailProductRun() {
        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreManager.getCurrentID().collect{
                getDetailProduct(it?.id.toString())
                getVariant(it?.id.toString())
            }
        }
    }

    fun getVariant(id: String){
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.ProductApi.GetVariant(id)
                if (response.isSuccessful && response.body() != null) {
                    withContext(Dispatchers.Main) {
                        Log.d("getVariant", "getVariant:${response} ")
                        val  NameVariant= response.body()!!.variantData?.rows?.get(0)?.variant_name
                        val  ImageVariant = response.body()!!.variantData?.rows?.get(0)?.variant_image


                        binding.NameVariant.text=NameVariant

                        val prefix1 = ImageVariant?.let { Utils.extractPrefix(it) }
                        binding.variantImg.setImageBitmap(prefix1?.let { Utils.decodeBase64ToBitmap(it) })
                    }
                }
            }catch (e: HttpException) {
                Toast.makeText(this@DetailPrActivity, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(this@DetailPrActivity, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }
        }
    }


    @SuppressLint("ResourceAsColor")
    fun getDetailProduct(id:String){
        lifecycleScope.launch(Dispatchers.IO) {
             try {
                 val response = RetrofitInstance.ProductApi.getDetailPr(id)
                 if (response.isSuccessful && response.body() != null) {
                     withContext(Dispatchers.Main) {
                         Log.d("getDetailProduct", "getDetailProduct:${response} ")
                         val  NameProduct= response.body()!!.productData?.product_name
//                    val  DecsProduct= response.body()!!.productData?.rows?.get(0)?.product_decs
                         val  PriceProduct = response.body()!!.productData?.product_price
                         val  ImageProduct = response.body()!!.productData?.product_image

                         val NameShop = response.body()!!.productData?.shop?.shop_name
                         val ImageShop = response.body()!!.productData?.shop?.Image_shop
                         val statusShop= response.body()!!.productData?.shop?.status


                         val prefix1 = ImageShop?.let { Utils.extractPrefix(it) }

                         //set data shop
                         Log.d("ShopImage", "getDetailProduct: ${prefix1}")
                         binding.imgShopDetai.setImageBitmap(prefix1?.let { Utils.decodeBase64ToBitmap(it) })
                         binding.namePRShop.text=NameShop
                         if (statusShop=="1"){
                             binding.colorStatus.setBackgroundColor(R.color.lightGreen)
                             binding.CheckStatus.text= "Online"
                         }else{
                             binding.colorStatus.setBackgroundColor(R.color.red)
                             binding.CheckStatus.text= "Offline"
                         }



                         //set data product
                         binding.nameProduct.text = NameProduct
                         binding.ProductPriceDT.text = PriceProduct.toString()
                         binding.edtSlgCart.text ="1"


                         val prefix = ImageProduct?.let { Utils.extractPrefix(it) }

                         binding.imageFiller1.setImageBitmap(prefix?.let { Utils.decodeBase64ToBitmap(it) })

                         // This block executes only if the response is successful and has a body
                     }
                 }
            } catch (e: HttpException) {
                Toast.makeText(this@DetailPrActivity, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(this@DetailPrActivity, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }

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
                                it.updatedAt,
                                it.variant_name,
                                it.numberSell
                            )
                        }

                        withContext(Dispatchers.Main) {
                            productAdapter.submitList(listProduct)
                            binding.recyclerSPDetail.apply {
                                layoutManager = GridLayoutManager(this@DetailPrActivity, 2)
                                setHasFixedSize(true)
                                adapter = productAdapter
                            }
                        }
                    }
                }
            } catch (e: HttpException) {
                Toast.makeText(this@DetailPrActivity, "http error ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(this@DetailPrActivity, "app error ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }
        }
    }

}