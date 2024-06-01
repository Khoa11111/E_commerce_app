package com.example.e_commerce_app.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.example.e_commerce_app.model.Category

import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.databinding.ItemCategorySpinnerBinding
import com.example.e_commerce_app.model.Product

class CategoryAdapterSpinner(
    context: Context,
    listCategory: List<Category>
) : ArrayAdapter<Category>(context, 0, listCategory) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_category_spinner, parent, false)

        getItem(position)?.let {
            view.findViewById<TextView>(R.id.tv_selected_spinner_category_name).apply {
                text = it.category_name
            }

            view.findViewById<TextView>(R.id.tv_selected_spinner_category_id).apply {
                text = it.id.toString()
            }
        }

        return view
    }
}