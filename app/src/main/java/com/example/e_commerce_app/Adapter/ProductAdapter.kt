package com.example.e_commerce_app.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ProductItemHomeBinding
import com.example.e_commerce_app.model.Product
import com.example.e_commerce_app.ui.DetailPrActivity
import com.example.e_commerce_app.ui.RegisterSellerActivity
import com.example.e_commerce_app.util.Utils

class ProductAdapter(val onclick: ProductOnItemClick) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(DiffUtil()) {

    class ProductViewHolder(val binding: ProductItemHomeBinding, val onclick: ProductOnItemClick) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            binding.apply {
//                Log.d("checkImage", "bind: ${item.toString()}")

                val StringSplit= item.product_image.toString()
                val prefix = Utils.extractPrefix(StringSplit)
//                Log.d("prefix", "bind: "+item.product_image.toString())
                IMGPR.setImageBitmap(Utils.decodeBase64ToBitmap(prefix))
                TxtProductName.text = item.product_name
                txtProductPrice.text = item.product_price.toString()
                root.setOnClickListener {
                    onclick.onItemClick(item, position)
                }
            }
        }
    }

    class DiffUtil : ItemCallback<Product>() {
        override fun areItemsTheSame(p0: Product, p1: Product): Boolean {
            return p0.id == p1.id
        }

        override fun areContentsTheSame(p0: Product, p1: Product): Boolean {
            return p0 == p1
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ProductViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.product_item_home, p0, false)
        return ProductViewHolder(binding = ProductItemHomeBinding.bind(view), onclick)
    }

    override fun onBindViewHolder(p0: ProductViewHolder, p1: Int) {
        val item = getItem(p1)
        p0.bind(item)
    }

    interface ProductOnItemClick {
        fun onItemClick(product: Product, position: Int){
//            val intent = Intent(context, DetailPrActivity::class.java)
//            intent.putExtra("product", product)
//            context.startActivity(intent)
        }
    }
}