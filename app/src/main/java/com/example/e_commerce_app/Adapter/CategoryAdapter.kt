package com.example.e_commerce_app.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.CategoryItemBinding
import com.example.e_commerce_app.model.Category
import com.example.e_commerce_app.util.Utils

class CategoryAdapter(val onclick: CategoryOnItemClick) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(DiffUtil()) {

    class CategoryViewHolder(val binding: CategoryItemBinding, val onlick: CategoryOnItemClick) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category) {
            binding.apply {
                nameCategory.text = item.category_name
                imgCategory.setImageBitmap(item.category_image?.let { Utils.decodeBase64ToBitmap(it) })
                root.setOnClickListener {
                    onlick.onItemClick(item, position)
                }
            }
        }
    }

    class DiffUtil : ItemCallback<Category>() {
        override fun areItemsTheSame(p0: Category, p1: Category): Boolean {
            return p0.id == p1.id
        }

        override fun areContentsTheSame(p0: Category, p1: Category): Boolean {
            return p0 == p1
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CategoryViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.category_item, p0, false)
        return CategoryViewHolder(binding = CategoryItemBinding.bind(view), onclick)
    }

    override fun onBindViewHolder(p0: CategoryViewHolder, p1: Int) {
        val item = getItem(p1)
        p0.bind(item)
    }

    interface CategoryOnItemClick {
        fun onItemClick(category: Category, position: Int)
    }
}