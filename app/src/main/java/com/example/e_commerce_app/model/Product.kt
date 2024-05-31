package com.example.e_commerce_app.model

import android.os.Parcel
import android.os.Parcelable

data class Product(
    val id: Int,
    val product_name: String,
    val product_decs: String,
    val category_id: Int,
    val status: String,
    val id_shop: Int,
    val product_review: Int,
    val product_price: Int,
    val product_image: String,
    val shop: Shop,
    val category: Category,
    val createdAt: String,
    val updatedAt: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readParcelable(Shop::class.java.classLoader)!!,
        parcel.readParcelable(Category::class.java.classLoader)!!,
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(product_name)
        parcel.writeString(product_decs)
        parcel.writeInt(category_id)
        parcel.writeString(status)
        parcel.writeInt(id_shop)
        parcel.writeInt(product_review)
        parcel.writeInt(product_price)
        parcel.writeString(product_image)
        parcel.writeParcelable(shop, flags)
        parcel.writeParcelable(category, flags)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}