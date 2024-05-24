package com.example.e_commerce_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.FragmentLoginBinding
import com.example.e_commerce_app.model.User
import com.example.e_commerce_app.util.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class LoginFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        // Onclick event here
        binding.goToSignup.setOnClickListener(this)
        binding.btnSignin.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.goToSignup -> v.findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
            binding.btnSignin -> login(v)
        }
    }

    private fun login(view: View) {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                val user = User(null, null, binding.inputPassWord.text.toString()
                    , binding.inputEmailAddress.text.toString(), null, null, null, null, null)
                RetrofitInstance.UserApi.login(user)
            } catch (e: HttpException) {
                Toast.makeText(requireContext(), "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    if (response.body()!!.err.toString() == "1"){
                        Toast.makeText(requireContext(), "Login fail, Please try again!", Toast.LENGTH_SHORT).show()
                    } else {
//                        DataLocalManager.setIdUser()
                        view.findNavController().navigate(R.id.action_loginFragment_to_homeActivity)
                    }
                }
            }
        }
    }

}