package com.example.e_commerce_app.ui

import android.content.Intent
import android.location.Address
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce_app.databinding.ActivityEditProfileBinding
import com.example.e_commerce_app.util.RetrofitInstance
import com.example.e_commerce_app.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.jar.Attributes.Name

class EditProfileActivity : AppCompatActivity() {
    lateinit var binding:ActivityEditProfileBinding
    private var uri: Uri? = null
    private var base64String: String = ""

    private val imageContract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            uri = it
            binding.imgProfile.setImageURI(uri)
            base64String = Utils.encodeUriToBase64(uri!!, applicationContext)
//            Log.d("check", base64String.length.toString())
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        getUser()

        binding.cvIMGProfile.setOnClickListener{
            imageContract.launch("image/*")
        }

        binding.SubmitAdd.setOnClickListener{
            UpdateProfile()
        }

    }

    private fun UpdateProfile() {
        TODO("Not yet implemented")
    }


    fun getUser(id: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.UserApi.getUserSearch(id)
            } catch (e: HttpException) {
                Toast.makeText(this@EditProfileActivity, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(this@EditProfileActivity, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    val Name= response.body()!!.userData?.Name
                    val Address = response.body()!!.userData?.Address
                    val ImageProfile = response.body()!!.userData?.imgUS
                    binding.prEditProfile.setText(Name)
                    binding.prEmailAddress.setText(Address)

                    val prefix = ImageProfile?.let { Utils.extractPrefix(it) }

                    binding.imgProfile.setImageBitmap(prefix?.let { Utils.decodeBase64ToBitmap(it) })

                    // This block executes only if the response is successful and has a body
                }
            }
        }

    }



}