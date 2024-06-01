package com.example.e_commerce_app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.FragmentUserInfoBinding
import com.example.e_commerce_app.datastore.DataStoreManager
import com.example.e_commerce_app.datastore.DataStoreProvider
import com.example.e_commerce_app.model.User
import com.example.e_commerce_app.ui.ProfileActivity
import com.example.e_commerce_app.ui.RGSellerOtpActivity
import com.example.e_commerce_app.ui.RegisterSellerActivity
import com.example.e_commerce_app.ui.ShopOwnActivity
import com.example.e_commerce_app.util.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class UserInfoFragment : Fragment() {

    private lateinit var binding: FragmentUserInfoBinding
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        dataStoreManager = DataStoreProvider.getInstance(requireContext())

        binding.cvStore.setOnClickListener {
            getUserRun()
        }
//
//        binding.frprMyOrder.setOnClickListener{
//
//        }
//
        binding.frprProfile.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

//    override fun onClick(v: View?) {
//        when (v) {
//            binding.cvStore -> getUser(v,"vanphattk147@gmail.com")
////            binding.frprMyOrder -> login(v)
////            binding.frprProfile
//        }
//    }

    private fun getUserRun() {
        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreManager.getCurrentUser().collect {
                getUser(it.id.toString())
            }
        }
    }

    fun getUser(id: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.UserApi.getUserSearch(id)
            } catch (e: HttpException) {
                Toast.makeText(context, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(context, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }
            Log.d("checkuserssssss", "getUser: ${response}")
            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    Log.d("userssssss", "getUser: " + response.body()!!.userData?.role + "get")

                    if (response.body()!!.userData?.role == "3") {
                        val intent = Intent(requireContext(), RegisterSellerActivity::class.java)
                        startActivity(intent)
                    }
                    if (response.body()!!.userData?.role == "2") {
                        val intent = Intent(requireContext(), ShopOwnActivity::class.java)
                        startActivity(intent)
                    }
                    if (response.body()!!.userData?.role == "1") {
                        val alertDialog = context?.let {
                            AlertDialog.Builder(it)
                                .setTitle("Access Denied!")
                                .setMessage(
                                    "\n" +
                                            "You not allow to access this"
                                )
                                .setPositiveButton("OK") { dialog, which ->
                                    // Handle positive button click
                                    dialog.dismiss()
                                }
                                .create()
                        }

                        alertDialog?.show()
                    }
                    // This block executes only if the response is successful and has a body
                }
            } else {
                Log.d("userssssss", "cos asala")
            }
        }
    }
}