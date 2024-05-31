package com.example.e_commerce_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.e_commerce_app.model.Category

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ItemCategorySpinnerBinding

class CategoryAdapterSpinner(val onClick: CategoryAdapterSpinnerClick) : ListAdapter<Category,CategoryAdapterSpinner.CategorySpinnerViewHolder>(
    CategoryAdapter.DiffUtil()
) {

    class CategorySpinnerViewHolder(val binding: ItemCategorySpinnerBinding, val onClick: CategoryAdapterSpinnerClick) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category) {
            binding.apply {
                tvSelectedSpinnerCategoryId.text = item.id.toString()
                tvSelectedSpinnerCategoryName.text = item.category_name
                root.setOnClickListener {
                    onClick.onItemClick(item, position)
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
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategorySpinnerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategorySpinnerViewHolder(binding = ItemCategorySpinnerBinding.bind(view), onClick)
    }

    override fun onBindViewHolder(holder: CategorySpinnerViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    interface CategoryAdapterSpinnerClick {
        fun onItemClick(category: Category, position: Int)
    }
}

