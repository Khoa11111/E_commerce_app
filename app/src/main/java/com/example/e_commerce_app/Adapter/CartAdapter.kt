package com.example.e_commerce_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.e_commerce_app.model.Cart
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ItemCartBinding
import com.example.e_commerce_app.util.Utils

class CartAdapter(val onClick: ProductCartonClick) : ListAdapter<Cart, CartAdapter.CartViewProduct>(DiffUtil()) {
    class CartViewProduct(val binding: ItemCartBinding, val onclick: ProductCartonClick) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Cart) {
            binding.apply {
                val StringSplit= item.productCartData?.product_image
                val prefix = Utils.extractPrefix(StringSplit.toString())
                binding.imgCartt.setImageBitmap(Utils.decodeBase64ToBitmap(prefix))
                binding.nameCartt.text= item.productCartData!!.product_name
                binding.giaCartt.text= item.price.toString()
                binding.edtSlCartt.text=item.soLuongMua.toString()
                root.setOnClickListener {
                    onclick.onItemClick(item, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewProduct {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewProduct(binding = ItemCartBinding.bind(view), onClick)
    }

    override fun onBindViewHolder(holder: CartViewProduct, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class DiffUtil : ItemCallback<Cart>() {
        override fun areItemsTheSame(p0: Cart, p1: Cart): Boolean {
            return p0.id == p1.id
        }

        override fun areContentsTheSame(p0: Cart, p1: Cart): Boolean {
            return p0 == p1
        }
    }

    interface ProductCartonClick {
        fun onItemClick(cart: Cart, position: Int)
    }

}
