package com.example.e_commerce_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.FragmentLoginBinding

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

        return binding.root
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.goToSignup -> v.findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

}