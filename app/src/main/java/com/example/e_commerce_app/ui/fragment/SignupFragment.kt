package com.example.e_commerce_app.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.FragmentSignupBinding
import com.example.e_commerce_app.model.User
import com.example.e_commerce_app.util.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class SignupFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)

        // OnClick event here
        binding.btnSignup.setOnClickListener(this)
        binding.goToLogin.setOnClickListener(this)

        return binding.root

    }

    override fun onClick(v: View?) {
        when(v) {
            binding.btnSignup -> {
                register(v,binding.inputName.text.toString(), binding.inputEmailAddress.text.toString(), binding.inputPassWord.text.toString(), binding.inputPhoneNumber.text.toString())
            }
            binding.goToLogin -> v.findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }

    fun register(view: View, name: String, email: String, password: String, phoneNumber: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                val user = User(null, name, password, email, phoneNumber, null, null, null, null)
                RetrofitInstance.UserApi.register(user)
            } catch (e: HttpException) {
                Toast.makeText(requireContext(), "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    view.findNavController().navigate(R.id.action_signupFragment_to_otpCormfirmFragment)
                }
            }
        }
    }
}