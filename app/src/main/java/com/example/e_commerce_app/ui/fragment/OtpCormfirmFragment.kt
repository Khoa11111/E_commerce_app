package com.example.e_commerce_app.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.FragmentOtpCormfirmBinding
import com.example.e_commerce_app.util.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class OtpCormfirmFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentOtpCormfirmBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOtpCormfirmBinding.inflate(inflater, container, false)

        // OnClick event here
        binding.back.setOnClickListener(this)
        binding.btnOTPVerify.setOnClickListener(this)

        // TextChanged event here
        moveToNextOTPNumber()

        return binding.root
    }

    // Hide keyboard
    private fun hideKeyboard(context: Context?, view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // Move to next Otp number when enter text
    private fun moveToNextOTPNumber(){
        binding.num1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.num1.text.toString().length == 1) {
                    if (binding.num2.text.isEmpty())
                        binding.num2.requestFocus()
                    else {
                        binding.num1.clearFocus()
                        hideKeyboard(context, binding.num1)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.num2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.num2.text.toString().length == 1) {
                    if (binding.num3.text.isEmpty())
                        binding.num3.requestFocus()
                    else {
                        binding.num2.clearFocus()
                        hideKeyboard(context, binding.num1)
                    }
                } else if (binding.num2.text.toString().length == 0)
                    binding.num1.requestFocus()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.num3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.num3.text.toString().length == 1) {
                    if (binding.num4.text.isEmpty())
                        binding.num4.requestFocus()
                    else {
                        binding.num3.clearFocus()
                        hideKeyboard(context, binding.num1)
                    }
                } else if (binding.num3.text.toString().length == 0)
                    binding.num2.requestFocus()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.num4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.num4.text.toString().length == 1) {
                    if (binding.num5.text.isEmpty())
                        binding.num5.requestFocus()
                    else {
                        binding.num4.clearFocus()
                        hideKeyboard(context, binding.num1)
                    }
                } else if (binding.num4.text.toString().length == 0)
                    binding.num3.requestFocus()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.num5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.num5.text.toString().length == 1) {
                    if (binding.num6.text.isEmpty())
                        binding.num6.requestFocus()
                    else {
                        binding.num5.clearFocus()
                        hideKeyboard(context, binding.num1)
                    }
                } else if (binding.num5.text.toString().length == 0)
                    binding.num4.requestFocus()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.num6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.num6.text.toString().length == 1) {
                    binding.num6.clearFocus()
                    hideKeyboard(context, binding.num6)
                } else if (binding.num6.text.toString().length == 0)
                    binding.num5.requestFocus()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.back -> v.findNavController().navigate(R.id.action_otpCormfirmFragment_to_signupFragment)
            binding.btnOTPVerify -> ConfirmOTP(v)
        }
    }

    // get OTP from UI
    private fun getOTP(): String {
        binding.apply {
            val otp = num1.text.toString() + num2.text.toString() + num3.text.toString() + num4.text.toString() + num5.text.toString() + num6.text.toString()
            return otp
        }
    }

    // Check if otp is correct
    private fun ConfirmOTP(view :View) {
        val otp = getOTP()
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.UserApi.confirmOTP(otp)
            } catch (e: HttpException) {
                Toast.makeText(requireContext(), "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(requireContext(), "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    if (response.body()!!.err.toString() == "1") {
                        Toast.makeText(requireContext(), "Your otp is incorrect. Please try again!", Toast.LENGTH_SHORT).show()
                    } else {
                        view.findNavController().navigate(R.id.action_otpCormfirmFragment_to_loginFragment)
                    }
                }
            }
        }
    }

}