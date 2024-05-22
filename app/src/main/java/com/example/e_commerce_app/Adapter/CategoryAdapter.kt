package com.example.e_commerce_app.Adapter

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.model.Category
import com.example.e_commerce_app.util.Utils

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    lateinit var categoryImage: ImageView
    lateinit var categoryTitle: TextView

    private val differCallback = object : DiffUtil.ItemCallback<Category>(){
        override fun areItemsTheSame(p0: Category, p1: Category): Boolean {
            return p0.id == p1.id
        }

        override fun areContentsTheSame(p0: Category, p1: Category): Boolean {
            return p0 == p1
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(p0.context).inflate(R.layout.category_item, p0, false),
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener : ((Category) -> Unit)? = null

    override fun onBindViewHolder(p0: CategoryViewHolder, p1: Int) {
        val category = differ.currentList[p1]

        categoryImage = p0.itemView.findViewById(R.id.img_category)
        categoryTitle = p0.itemView.findViewById(R.id.name_category)

        p0.itemView.apply {
            val bitmap = category.category_image?.let { Utils.decodeBase64ToUri(it) }
            categoryImage.setImageBitmap(bitmap)
            categoryTitle.text = category.category_name

            setOnClickListener {
                onItemClickListener?.let {
                    it(category)
                }
                Log.d("item", it.id.toString())
            }
        }
    }


}