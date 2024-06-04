package com.example.e_commerce_app.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.FragmentLoginBinding
import com.example.e_commerce_app.datastore.DataStoreManager
import com.example.e_commerce_app.datastore.DataStoreProvider
import com.example.e_commerce_app.model.User
import com.example.e_commerce_app.ui.LoginSignupActivity
import com.example.e_commerce_app.util.RetrofitInstance
import com.example.e_commerce_app.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class LoginFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        dataStoreManager = DataStoreProvider.getInstance(requireContext())

        // Onclick event here
        binding.goToSignup.setOnClickListener(this)
        binding.btnSignin.setOnClickListener(this)
        binding.chagePassword.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.goToSignup -> v.findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
            binding.btnSignin -> login(v)
            binding.chagePassword -> v.findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
    }

    private fun login(view: View) {
        val user = User(
            null,
            null,
            binding.inputPassWord.text.toString(),
            binding.inputEmailAddress.text.toString(),
            null,
            null,
            null,
            null,
            null,
            null
        )
        lifecycleScope.launch {
            val response = RetrofitInstance.UserApi.login(user)

            if (response.isSuccessful && response.body() != null) {
                if (response.body()!!.err.toString() == "1") {
                    Toast.makeText(requireContext(), "Please try login again!", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val id = response.body()!!.userData!!.id
                val email = response.body()!!.userData!!.email
                val role = response.body()!!.userData!!.role
                val shop = response.body()!!.userData!!.shop
                val name = response.body()!!.userData?.Name
                Log.d("CheckLogin", "login: ${shop}")
                if (id != null && role != null) {
                    dataStoreManager.storeCurrenUser(
                        User(
                            id,
                            name,
                            null,
                            email,
                            null,
                            null,
                            null,
                            null,
                            role,
                            shop
                        )
                    )
                    shop?.let {
                        dataStoreManager.storeShop(
                            it
                        )
                    }
                }
                view.findNavController().navigate(R.id.action_loginFragment_to_homeActivity)
            } else {
                Toast.makeText(requireContext(), "Please try login again!", Toast.LENGTH_SHORT).show()
                return@launch
            }
        }
    }

}