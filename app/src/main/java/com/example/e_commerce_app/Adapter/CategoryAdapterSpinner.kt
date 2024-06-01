package com.example.e_commerce_app.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.DiffUtil
import com.example.e_commerce_app.model.Category

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ItemCategorySpinnerBinding

class CategoryAdapterSpinner(
    context: Context,
    private val onClick: CategoryOnClick
) : ArrayAdapter<Category>(context, 0) {

    private val categories: MutableList<Category> = mutableListOf()

    fun addAll(newCategories: List<Category>) {
        categories.clear()
        categories.addAll(newCategories)
        notifyDataSetChanged()
    }

    override fun getCount(): Int = categories.size

    override fun getItem(position: Int): Category? = categories[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(convertView, parent, position)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(convertView, parent, position)
    }

    private fun createViewFromResource(convertView: View?, parent: ViewGroup, position: Int): View {
        val binding = if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            ItemCategorySpinnerBinding.inflate(inflater, parent, false)
        } else {
            ItemCategorySpinnerBinding.bind(convertView)
        }

        val category = categories[position]
        binding.tvSelectedSpinnerCategoryId.text = category.id.toString()
        binding.tvSelectedSpinnerCategoryName.text = category.category_name

        binding.root.setOnClickListener {
            onClick.onItemClick(category, position)
        }

        return binding.root
    }

    interface CategoryOnClick {
        fun onItemClick(category: Category, position: Int)
    }
}