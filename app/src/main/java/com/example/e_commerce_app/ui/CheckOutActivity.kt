package com.example.e_commerce_app.ui

import android.content.ClipData.Item
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_commerce_app.Adapter.CartAdapter
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityCheckOutBinding
import com.example.e_commerce_app.datastore.DataStoreManager
import com.example.e_commerce_app.datastore.DataStoreProvider
import com.example.e_commerce_app.model.Cart
import com.example.e_commerce_app.model.Product
import com.example.e_commerce_app.model.ProductCartData
import com.example.e_commerce_app.model.Shop
import com.example.e_commerce_app.requestModel.ItemRequest
import com.example.e_commerce_app.requestModel.OrderRequest
import com.example.e_commerce_app.util.RetrofitInstance
import com.example.e_commerce_app.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class CheckOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckOutBinding
    lateinit var cartAdapter: CartAdapter
    lateinit var dataStoreManager: DataStoreManager
    var payment_method = ""

    private val ProductCartonClick by lazy {
        object : CartAdapter.ProductCartonClick {
            override fun onItemClick(view: View, cart: Cart, position: Int) {

            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataStoreManager = DataStoreProvider.getInstance(this)


        getAllCartRun()

        binding.UpdateAddressOrder.setOnClickListener{
            createAddressPhoneDialog(this) { address, phoneNumber ->
                // Process the entered address and phone number here
                binding.UpdateAddressOrder.setText(address)
                binding.txtSDT.setText(phoneNumber)
            }
        }

        binding.ComfirmOrder.setOnClickListener{
            finish()
        }

        binding.btnOrderComplete.setOnClickListener{
            val Address_ship= binding.UpdateAddressOrder.text.toString()
            val Phone= binding.txtSDT.text.toString()
            if (Address_ship=="Điạ chỉ" || Phone=="SDT"){
                val alertDialog = AlertDialog.Builder(this@CheckOutActivity)
                    .setTitle("CheckOut!")
                    .setMessage(
                        "\n" +
                                "You must input you phone and your address!!"
                    )
                    .setPositiveButton("OK") { dialog, which ->
                        // Handle positive button click
                        dialog.dismiss()
                        createAddressPhoneDialog(this) { address, phoneNumber ->
                            // Process the entered address and phone number here
                            binding.UpdateAddressOrder.setText(address)
                            binding.txtSDT.setText(phoneNumber)
                        }
                    }
                    .setNegativeButton("Cancel") { dialog, which ->
                        // Handle negative button click
                        dialog.dismiss()
                    }
                    .create()
                alertDialog.show()
            }else{
                RunOrder(Phone.toInt(),Address_ship)
            }
        }




        binding.txtWayPay.setOnClickListener{
            binding.txtMomo.isChecked=false
            payment_method ="OCD"
        }
        binding.txtMomo.setOnClickListener{
            binding.txtWayPay.isChecked=false
            payment_method ="MOMO"
        }
    }

//    private fun setAllMoney() {
//        lifecycleScope.launch(Dispatchers.IO) {
//            val cartAdapter: CartAdapter = binding.rcvOrder.adapter as CartAdapter
//            val cartList = cartAdapter.currentList
//            if (cartList.size != 0) {
//                val groupedProducts = groupProductsByShop(cartList)
//                var TotalMoney = 0
//                for (shopId in groupedProducts.keys) {
//                    val shopProducts = groupedProducts[shopId]!!
//                    for (cart in shopProducts) {
//                        TotalMoney= TotalMoney + cart.price!!
//                    }
//                }
//                withContext(Dispatchers.Main) {
//                    binding.txtMoneyFast.setText(TotalMoney.toString())
//                    binding.txtMoneyTotal.setText(TotalMoney.toString())
//                }
//            }
//        }
//    }

    fun createAddressPhoneDialog(context: Context, onPositiveClick: (address: String, phoneNumber: String) -> Unit) {
        val builder = AlertDialog.Builder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.address_phone_dialog, null) // Replace with your actual layout

        val addressEditText = view.findViewById<EditText>(R.id.edt_Address_checkout) // Replace with actual view IDs
        val phoneNumberEditText = view.findViewById<EditText>(R.id.edt_SDT_checkout) // Replace with actual view IDs

        builder.setView(view)
            .setPositiveButton("OK") { _, _ ->
                val address = addressEditText.text.toString()
                val phoneNumber = phoneNumberEditText.text.toString()
                onPositiveClick(address, phoneNumber)
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Handle cancel action if needed
            }
            .show()
    }


    private fun groupProductsByShop(listProducts: List<Cart>): Map<Int, List<Cart>> {
        return listProducts.groupBy { it.id_shop!! }
    }


    //
    // Access products for a specific shop


    private fun RunOrder(Phone: Int,Address_ship:String) {
            lifecycleScope.launch(Dispatchers.IO) {
                val cartAdapter: CartAdapter = binding.rcvOrder.adapter as CartAdapter
                val cartList = cartAdapter.currentList
                if (cartList.size != 0) {
                    val groupedProducts = groupProductsByShop(cartList)
                    dataStoreManager.getCurrentUser().collect{
                        for (shopId in groupedProducts.keys) {
                            val shopProducts = groupedProducts[shopId]!!
                            val listItem= ArrayList<ItemRequest>()
                            for (cart in shopProducts) {
                                val itemRequest= cart.variant_id?.let { it1 -> cart.soLuongMua?.let { it2 ->
                                    ItemRequest(it1,
                                        it2
                                    )
                                } }

                                itemRequest?.let { it1 -> listItem.add(it1) }
                            }
                            Log.d("CheckOrderList", "Order:${listItem} ")
                            it.id?.let { it1 -> Order(it1,Phone, Address_ship,listItem,payment_method) }
                        }
                    }
                }
            }
    }

    private fun Order(id_User: Int, Phone: Int, Address_ship: String, item:ArrayList<ItemRequest>,payment_method: String){
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val order= OrderRequest(id_User,Phone,Address_ship,item, payment_method)
                Log.d("CheckOrder", "order:${order} ")
                val response = RetrofitInstance.CartApi.Order(order)
                withContext(Dispatchers.Main){
                    if (response.isSuccessful && response.body() != null) {
                    Log.d("getAllOrder", "getAllCart: ${response.body()}")
                        if (response.body()!!.err.toString() == "0") {

                            val alertDialog = AlertDialog.Builder(this@CheckOutActivity)
                                .setTitle("Order!")
                                .setMessage(
                                    "\n" +
                                            "Order Complete!!"
                                )
                                .setPositiveButton("OK") { dialog, which ->
                                    // Handle positive button click
                                    dialog.dismiss()
                                    val intent = Intent(this@CheckOutActivity, HomeActivity::class.java)
                                    startActivity(intent)
                                }
//                            .setNegativeButton("Cancel") { dialog, which ->
//                                // Handle negative button click
//                                dialog.dismiss()
//                            }
                                .create()
                            alertDialog.show()
                        }else{
                            val alertDialog = AlertDialog.Builder(this@CheckOutActivity)
                                .setTitle("Order!")
                                .setMessage(
                                    "\n" +
                                            "Order Failed!!"
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
                    }
                }

            }catch (e: HttpException) {
                Toast.makeText(this@CheckOutActivity, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(this@CheckOutActivity, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }
        }
    }

    fun getAllCartRun() {
        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreManager.getCurrentUser().collect {
                cartAdapter = CartAdapter(ProductCartonClick)
                Log.d("id_userssss", "getAllCartRun: " + it.id.toString())
                it.id?.let { it1 -> getAllCart(it1, cartAdapter) }
                withContext(Dispatchers.Main){
                    binding.txtTen.setText(it.Name+" |")
                }
            }
//
        }
    }

    fun getAllCart(id: Int, cartAdapter: CartAdapter) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.CartApi.GetCart(id)
                if (response.isSuccessful && response.body() != null) {
//                    Log.d("getAllCart", "getAllCart: ${response.body()}")
                    if (response.body()!!.err.toString() == "0") {
                        val listCart = response.body()!!.dataCart?.rows?.map {
                            val shop = it.shop?.let { it1 ->
                                Shop(
                                    it.shop.id,
                                    it.shop.shop_name,
                                    it.shop.Image_shop,
                                    it.shop.Address,
                                    it.shop.id_user,
                                    it.shop.status,
                                    it.shop.createdAt,
                                    it.shop.updatedAt
                                )
                            }
                            val productCartData = it.productCartData?.let { it1 ->
                                ProductCartData(
                                    it.productCartData.category_id,
                                    it.productCartData.createdAt,
                                    it1.id,
                                    it.productCartData.id_shop,
                                    it.productCartData.product_decs,
                                    it.productCartData.product_image,
                                    it.productCartData.product_name,
                                    it.productCartData.product_price,
                                    it.productCartData.product_review,
                                    it.productCartData.status,
                                    it.productCartData.updatedAt
                                )
                            }
                            Cart(
                                it.createdAt,
                                it.id,
                                it.id_shop,
                                it.maSP,
                                it.price,
                                productCartData,
                                shop,
                                it.soLuongMua,
                                it.status,
                                it.uid,
                                it.updatedAt,
                                it.variant_id
                            )
                        }

                        withContext(Dispatchers.Main) {
                            cartAdapter.submitList(listCart)
                            binding.rcvOrder.apply {
                                layoutManager = GridLayoutManager(this@CheckOutActivity, 1)
                                setHasFixedSize(true)
                                adapter = cartAdapter
                            }
                        }
                    }
                }
            } catch (e: HttpException) {
                Toast.makeText(this@CheckOutActivity, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(this@CheckOutActivity, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }
        }
    }
}