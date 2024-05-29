package com.example.e_commerce_app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.FragmentUserInfoBinding
import com.example.e_commerce_app.model.User
import com.example.e_commerce_app.ui.RegisterSellerActivity

class UserInfoFragment : Fragment() {

    private lateinit var binding: FragmentUserInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)

        binding.cvStore.setOnClickListener{
            val intent = Intent(requireContext(), RegisterSellerActivity::class.java)
            startActivity(intent)
        }
//        val currentUser = arguments?.getParcelable<User>("currentUser")
//        Log.d("checkingUser", currentUser.toString())
        return binding.root
    }
}