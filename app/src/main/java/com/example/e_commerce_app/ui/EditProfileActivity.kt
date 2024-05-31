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
import com.example.e_commerce_app.datastore.DataStoreManager
import com.example.e_commerce_app.datastore.DataStoreProvider
import com.example.e_commerce_app.model.User
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
    private lateinit var dataStoreManager: DataStoreManager
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

        getUserRun()

        dataStoreManager = DataStoreProvider.getInstance(this)

        binding.cvIMGProfile.setOnClickListener{
            imageContract.launch("image/*")
        }

        binding.SubmitAdd.setOnClickListener{
            val NamePF= binding.prEditProfile.text.toString()
            val AddressPF=binding.prEmailAddress.text.toString()
            val PhonePF=binding.prPhoneNumber.text.toString()
            UpdateProfile(NamePF,AddressPF,PhonePF,base64String)
        }

    }

    private fun UpdateProfile(Name:String?,Address:String,Phone:String,ImgUs:String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                dataStoreManager.getCurrentUser().collect{
                    var user : User?=null
                    if (ImgUs==""){
                         user = User(it.id,Name,null,it.email,Phone,Address,null,null,null)
                        Log.d("Usersss", "UpdateProfile:${user} ")
                    }else  {
                         user = User(it.id,Name,null,it.email,Phone,Address,base64String,null,null)
                        Log.d("Usersss", "UpdateProfile:${user} ")
                    }
                    val response = RetrofitInstance.UserApi.UpdateUser(it.id.toString(),user)
                    if (response.isSuccessful && response.body() != null) {
                        withContext(Dispatchers.Main) {
                            val alertDialog = AlertDialog.Builder(this@EditProfileActivity)
                                .setTitle("Edit Profile")
                                .setMessage(
                                    "\n" +
                                            "Change info success!"
                                )
                                .setPositiveButton("OK") { dialog, which ->
                                    dialog.dismiss()
                                    val intent = Intent(applicationContext, ProfileActivity::class.java)
                                    startActivity(intent)
                                }
//                            }
                                .create()
                            alertDialog.show()
                        }
                    }else{
                        val alertDialog = AlertDialog.Builder(this@EditProfileActivity)
                            .setTitle("Edit Profile")
                            .setMessage(
                                "\n" +
                                        "Change info Failed!!!"
                            )
                            .setPositiveButton("OK") { dialog, which ->
                                dialog.dismiss()
                            }
//                            }
                            .create()
                        alertDialog.show()
                    }

                }
            }catch (e: HttpException) {
                Toast.makeText(this@EditProfileActivity, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(this@EditProfileActivity, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }
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
                    val Phone = response.body()!!.userData?.SDT
                    val ImageProfile = response.body()!!.userData?.imgUS
                    binding.prEditProfile.setText(Name)
                    binding.prEmailAddress.setText(Address)
                    binding.prPhoneNumber.setText(Phone)
                    val prefix = ImageProfile?.let { Utils.extractPrefix(it) }

                    binding.imgProfile.setImageBitmap(prefix?.let { Utils.decodeBase64ToBitmap(it) })

                    // This block executes only if the response is successful and has a body
                }
            }
        }

    }



}