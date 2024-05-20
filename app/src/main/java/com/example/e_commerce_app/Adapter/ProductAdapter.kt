package com.example.e_commerce_app.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.model.Product

class ProductAdapter(val activity: Activity) :RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {
    private var productList: List<Product>?=null
    fun setProducts(productList: List<Product>?){
        this.productList = productList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item_home,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductAdapter.MyViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        if(productList==null)return 0
        else return productList?.size!!
    }

    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val ProductName= view.findViewById<View>(R.id.TxtProductName)
    }

}