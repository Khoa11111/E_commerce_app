package com.example.e_commerce_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ItemProductShopBinding
import com.example.e_commerce_app.model.Product
import com.example.e_commerce_app.util.Utils

class ProductShopAdapter(val onclick: ProductOnItemClick) : ListAdapter<Product, ProductShopAdapter.ProductShopViewHolder>(DiffUtil())  {

    class ProductShopViewHolder(val binding: ItemProductShopBinding, val onclick: ProductOnItemClick) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            binding.apply {

                val StringSplit= item.product_image.toString()
                val prefix = Utils.extractPrefix(StringSplit)
//                Log.d("prefix", "bind: "+item.product_image.toString())
                ImgVIewProductShop.setImageBitmap(Utils.decodeBase64ToBitmap(prefix))
                TxtProductNameShop.text = item.product_name
                txtProductPriceShop.text = item.product_price.toString()
                root.setOnClickListener {
                    onclick.onItemClick(item, position)
                }
            }
        }
    }
    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(p0: Product, p1: Product): Boolean {
            return p0.id == p1.id
        }

        override fun areContentsTheSame(p0: Product, p1: Product): Boolean {
            return p0 == p1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductShopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_shop, parent, false)
        return ProductShopViewHolder(binding = ItemProductShopBinding.bind(view), onclick)
    }

    override fun onBindViewHolder(holder: ProductShopViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    interface ProductOnItemClick {
        fun onItemClick(product: Product, position: Int){
//            val intent = Intent(context, DetailPrActivity::class.java)
//            intent.putExtra("product", product)
//            context.startActivity(intent)
        }
    }
}