package com.example.e_commerce_app.Repositories

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.e_commerce_app.model.User
import com.example.e_commerce_app.util.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ProductRepository(private val context: Context) {

//    fun getProduct(){
//        GlobalScope.launch(Dispatchers.IO) {
//            val response = try {
//                RetrofitInstance.UserApi.getAllProduct()
//            } catch (e: HttpException) {
//                Toast.makeText(context, "http error: ${e.message}", Toast.LENGTH_LONG).show()
//                return@launch
//            } catch (e: IOException) {
//                Toast.makeText(context, "app error: ${e.message}", Toast.LENGTH_LONG).show()
//                return@launch
//            }
//            if (response.isSuccessful && response.body() != null) {
//
//            }
//        }
//
//    }

//    fun register(view: View, name: String, email: String, password: String, phoneNumber: String) {
//        GlobalScope.launch(Dispatchers.IO) {
//            val response = try {
//                val user = User(null, name, password, email, phoneNumber, null, null, null)
//                RetrofitInstance.UserApi.register(user)
//            } catch (e: HttpException) {
//                Toast.makeText(requireContext(), "http error: ${e.message}", Toast.LENGTH_LONG).show()
//                return@launch
//            } catch (e: IOException) {
//                Toast.makeText(requireContext(), "app error: ${e.message}", Toast.LENGTH_LONG).show()
//                return@launch
//            }
//
//            if (response.isSuccessful && response.body() != null) {
//                withContext(Dispatchers.Main) {
//                    binding.apply {
//                        Log.d("check", response.body()!!.mes)
//                    }
//                }
////                view.findNavController().navigate(R.id.action_signupFragment_to_otpCormfirmFragment)
//            }
//        }
//    }
}