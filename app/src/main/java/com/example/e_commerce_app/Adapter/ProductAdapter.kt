package com.example.e_commerce_app.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.model.Product
import com.example.e_commerce_app.model.ProductData

class ProductAdapter(private val productList:ArrayList<Product>) :RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item_home,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductAdapter.MyViewHolder, position: Int) {
        val currentItem=productList[position]
        holder.TXTProductName.text=currentItem.product_name
        holder.TxtProductPrice.text= currentItem.product_price.toString()
    }

    override fun getItemCount(): Int {
        if(productList==null)return 0
        else return productList?.size!!
    }

    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val IMGPR : ImageView = view.findViewById(R.id.IMGPR)
        val TXTProductName:TextView=view.findViewById(R.id.TxtProductName)
        val TxtProductPrice:TextView=view.findViewById(R.id.txtProductPrice)
    }

}