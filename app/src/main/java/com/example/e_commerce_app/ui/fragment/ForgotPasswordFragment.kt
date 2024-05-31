package com.example.e_commerce_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.FragmentForgotPasswordBinding
import com.example.e_commerce_app.request.EmailRequest
import com.example.e_commerce_app.request.RecoveryPasswordRequest
import com.example.e_commerce_app.util.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForgotPasswordFragment : Fragment(), OnClickListener {
    private lateinit var binding: FragmentForgotPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)

        binding.apply {
            tvGoToLogin.setOnClickListener(this@ForgotPasswordFragment)
            btnSendOtp.setOnClickListener(this@ForgotPasswordFragment)
            btnRecoveryPass.setOnClickListener(this@ForgotPasswordFragment)
        }

        return binding.root
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.tvGoToLogin -> v.findNavController().navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
            binding.btnSendOtp -> sendOtp(binding.edtEmail.text.toString().trim())
            binding.btnRecoveryPass -> recoveryPassword(v)
        }
    }

    private fun recoveryPassword(view: View) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (checkPassword(
                    binding.edtNewPass.text.toString().trim(),
                    binding.edtConfirmPass.text.toString().trim()
                )
            ) {
                val response = RetrofitInstance.UserApi.recoveryPassword(
                    RecoveryPasswordRequest(
                        binding.edtEmail.text.toString().trim(),
                        binding.edtOtp.text.toString().trim().toInt(),
                        binding.edtNewPass.text.toString().trim()
                    )
                )
                if (!response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                } else if (response.body()!!.err.toString() == "0") {
                    withContext(Dispatchers.Main) {
                        view.findNavController().navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Otp is incorrect", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun sendOtp(email: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            if (email.length == 0 || email.isEmpty()) {
                withContext(Dispatchers.Main) {
                    binding.edtEmail.error = "Can't be empty"
                }
            } else {
                val response = RetrofitInstance.UserApi.sendOtpRecoveryPassword(EmailRequest(email))
                if (!response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }
                } else if (response.body()!!.err.toString() == "0") {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Had send otp", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun checkPassword(newPass: String, confirmPass: String): Boolean {
        if (newPass.equals(confirmPass)) {
            return true
        }
        return false
    }
}
