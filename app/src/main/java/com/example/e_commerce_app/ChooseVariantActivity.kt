package com.example.e_commerce_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce_app.databinding.ActivityChooseVariantBinding
import com.example.e_commerce_app.datastore.DataStoreManager
import com.example.e_commerce_app.util.RetrofitInstance
import com.example.e_commerce_app.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class ChooseVariantActivity : AppCompatActivity() {
    lateinit var binding: ActivityChooseVariantBinding
    private lateinit var dataStoreManager: DataStoreManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityChooseVariantBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    private fun getDetailProductRun() {
        lifecycleScope.launch(Dispatchers.IO) {
            dataStoreManager.getCurrentID().collect{
                getVariant(it?.id.toString())
            }
        }
    }

    fun getVariant(id: String){
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.ProductApi.GetVariant(id)
                if (response.isSuccessful && response.body() != null) {
                    withContext(Dispatchers.Main) {




                    }
                }
            }catch (e: HttpException) {
                Toast.makeText(this@ChooseVariantActivity, "http error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(this@ChooseVariantActivity, "app error: ${e.message}", Toast.LENGTH_LONG).show()
                return@launch
            }
        }
    }
}