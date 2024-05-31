package com.example.e_commerce_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ItemVariantBinding
import com.example.e_commerce_app.databinding.ProductItemHomeBinding
import com.example.e_commerce_app.model.Product
import com.example.e_commerce_app.model.Variant
import com.example.e_commerce_app.util.Utils

class VariantAdapter(val onClick:VariantOnClick): ListAdapter<Variant,VariantAdapter.VariantViewHolder>(DiffUtil()) {
    class VariantViewHolder(val binding: ItemVariantBinding, val onclick: VariantOnClick) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Variant) {
            binding.apply {
                binding.NameVariantItem.text=item.variant_name
                val VariantImage= item.variant_image
                val prefix = VariantImage?.let { Utils.extractPrefix(it) }
                binding.ImgVariant.setImageBitmap(prefix?.let { Utils.decodeBase64ToBitmap(it) })
                root.setOnClickListener {
                    onclick.onItemClick(item, position)
                }
            }
        }
    }

    interface VariantOnClick {
        fun onItemClick(variant: Variant, position: Int)
    }

    class DiffUtil : ItemCallback<Variant>() {
        override fun areItemsTheSame(p0: Variant, p1: Variant): Boolean {
            return p0.id == p1.id
        }

        override fun areContentsTheSame(p0: Variant, p1: Variant): Boolean {
            return p0 == p1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item_home, parent, false)
        return VariantViewHolder(binding = ItemVariantBinding.bind(view), onClick)
    }

    override fun onBindViewHolder(holder: VariantViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}