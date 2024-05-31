package com.example.e_commerce_app.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ActivityProfileBinding
import com.example.e_commerce_app.datastore.DataStoreManager
import com.example.e_commerce_app.datastore.DataStoreProvider
import com.example.e_commerce_app.util.RetrofitInstance
import com.example.e_commerce_app.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    private lateinit var dataStoreManager: DataStoreManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getUserRun()
        dataStoreManager = DataStoreProvider.getInstance(this)

        binding.prEditProfile.setOnClickListener{
            val intent = Intent(applicationContext, EditProfileActivity::class.java)
            startActivity(intent)
        }
        binding.prSignOut.setOnClickListener{
            val intent = Intent(applicationContext, LoginSignupActivity::class.java)
            startActivity(intent)
        }
    }


    fun getUserRun(){
        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreManager.getCurrentUser().collect{
                getUser(it.id.toString())
            }
        }
    }
    fun getUser(id: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.UserApi.getUserSearch(id)
            } catch (e: HttpException) {
                Toast.makeText(this@ProfileActivity, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(this@ProfileActivity, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    val Name= response.body()!!.userData?.Name
                    val Address = response.body()!!.userData?.Address
                    val ImageProfile = response.body()!!.userData?.imgUS
                    val Email = response.body()!!.userData?.email
                    val Phone = response.body()!!.userData?.SDT
                    binding.profileFullname.text=Name
                    binding.prEmailAddress.text=Email
                    binding.prPhoneNumber.text=Phone
                    binding.prResidentialAddress.text=Address


                    val prefix = ImageProfile?.let { Utils.extractPrefix(it) }

                    binding.imgProfile.setImageBitmap(prefix?.let { Utils.decodeBase64ToBitmap(it) })

                    // This block executes only if the response is successful and has a body
                }
            }
        }

    }
}