package com.example.e_commerce_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.FragmentSignupBinding

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
            binding.btnSignup -> v.findNavController().navigate(R.id.action_signupFragment_to_otpCormfirmFragment)
            binding.goToLogin -> v.findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }

}