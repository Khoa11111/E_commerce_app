package com.example.e_commerce_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment(), OnClickListener {
    private lateinit var binding: FragmentForgotPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)

        binding.apply {
            tvGoToLogin.setOnClickListener(this@ForgotPasswordFragment)
        }

        return binding.root
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.tvGoToLogin -> v.findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

}