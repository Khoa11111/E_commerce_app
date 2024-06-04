package com.example.e_commerce_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ItemShopOrderBinding
import com.example.e_commerce_app.databinding.ProductItemHomeBinding
import com.example.e_commerce_app.model.Product
import com.example.e_commerce_app.util.Utils

//class OrderAdapter(val onClick: OrderOnClick):ListAdapter<Order, OrderAdapter.OrderViewHolder>(DiffUtil()) {
//    class ProductViewHolder(val binding: ItemShopOrderBinding, val onclick: ProductAdapter.ProductOnItemClick) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: Order) {
//            binding.apply {
////                Log.d("checkImage", "bind: ${item.toString()}")
//                root.setOnClickListener {
//                    onclick.onItemClick(item, position)
//                }
//            }
//        }
//    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAdapter.ProductViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item_home, parent, false)
//        return OrderViewHolder(binding = ProductItemHomeBinding.bind(view), onClick)
//    }
//
//    override fun onBindViewHolder(holder: OrderAdapter.ProductViewHolder, position: Int) {
//        TODO("Not yet implemented")
//    }
//}