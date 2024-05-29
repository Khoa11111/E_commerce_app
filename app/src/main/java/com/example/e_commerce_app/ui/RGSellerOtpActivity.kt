package com.example.e_commerce_app.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityRegisterSellerBinding
import com.example.e_commerce_app.databinding.ActivityRgsellerOtpBinding
import com.example.e_commerce_app.util.RetrofitInstance
import com.google.android.material.internal.ViewUtils.hideKeyboard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RGSellerOtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRgsellerOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRgsellerOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        moveToNextOTPNumber()

        binding.btnOTPVerify.setOnClickListener{
            ConfirmOTP()
        }
        binding.back.setOnClickListener{
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun hideKeyboard(context: Context?, view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

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
                        hideKeyboard(this@RGSellerOtpActivity, binding.num1)
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
                        hideKeyboard(this@RGSellerOtpActivity, binding.num1)
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
                        hideKeyboard(this@RGSellerOtpActivity, binding.num1)
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
                        hideKeyboard(this@RGSellerOtpActivity, binding.num1)
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
                        hideKeyboard(this@RGSellerOtpActivity, binding.num1)
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
                    hideKeyboard(this@RGSellerOtpActivity, binding.num6)
                } else if (binding.num6.text.toString().length == 0)
                    binding.num5.requestFocus()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun getOTP(): String {
        binding.apply {
            val otp = num1.text.toString() + num2.text.toString() + num3.text.toString() + num4.text.toString() + num5.text.toString() + num6.text.toString()
            return otp
        }
    }

    private fun ConfirmOTP() {
        val otp = getOTP()
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.UserApi.confirmOTPSeller(otp)
            } catch (e: HttpException) {
                Toast.makeText(applicationContext, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(applicationContext, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    if (response.body()!!.err.toString() == "1") {
                        Toast.makeText(applicationContext, "Your otp is incorrect. Please try again!", Toast.LENGTH_SHORT).show()
                    } else {
                        val alertDialog = AlertDialog.Builder(this@RGSellerOtpActivity)
                            .setTitle("Register Success!")
                            .setMessage("\n" +
                                    "You have successfully registered, please wait for approval from admin")
                            .setPositiveButton("OK") { dialog, which ->
                                // Handle positive button click
                                dialog.dismiss()
                                val intent = Intent(applicationContext, HomeActivity::class.java)
                                startActivity(intent)
                            }
//                            .setNegativeButton("Cancel") { dialog, which ->
//                                // Handle negative button click
//                                dialog.dismiss()
//                            }
                            .create()

                        alertDialog.show()
                    }
                }
            }
        }
    }
}