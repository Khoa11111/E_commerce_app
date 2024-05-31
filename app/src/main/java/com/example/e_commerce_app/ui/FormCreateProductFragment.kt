package com.example.e_commerce_app.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.e_commerce_app.databinding.FragmentFormCreateProductBinding

/**
 * A simple [Fragment] subclass.
 * Use the [FormCreateProductFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FormCreateProductFragment : DialogFragment() {
    private lateinit var listener: FormInputListener
    private lateinit var binding: FragmentFormCreateProductBinding

    interface FormInputListener {
        fun onFormSubmitted(inputText: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFormCreateProductBinding.inflate(inflater, container, false)


        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FormInputListener) {
            listener = context as FormInputListener
        } else {
            throw RuntimeException("$context must implement FormInputListener")
        }
    }
}