package com.example.e_commerce_app.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.model.Category
import com.example.e_commerce_app.model.Product

class ProductSearchAdapter(
    context: Context,
    private val suggestions: List<Product>
) : ArrayAdapter<Product>(context, 0, suggestions) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.search_item, parent, false)

        getItem(position)?.let {
            view.findViewById<TextView>(R.id.search_product_name).apply {
                text = it.product_name
            }

            view.findViewById<TextView>(R.id.search_product_id).apply {
                text = it.id.toString()
            }
        }

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val suggestionsList = mutableListOf<Product>()
                if (constraint != null) {
                    for (suggestion in suggestions) {
                        if (suggestion.product_name.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            suggestionsList.add(suggestion)
                        }
                    }
                }
                results.values = suggestionsList
                results.count = suggestionsList.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if ((results?.count ?: 0) > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }
}