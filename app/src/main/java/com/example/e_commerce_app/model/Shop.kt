package com.example.e_commerce_app.model

import android.os.Parcel
import android.os.Parcelable

data class Shop(
    val id: Int,
    val shop_name: String,
    val Image_shop: String?,
    val Address: String,
    val id_user: Int,
    val status: String,
    val createdAt : String?,
    val updatedAt: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        id?.let { dest.writeInt(it) }
        dest.writeString(shop_name)
        dest.writeString(Image_shop)
        dest.writeString(Address)
        dest.writeString(id_user.toString())
        dest.writeString(status)
        dest.writeString(createdAt)
        dest.writeString(updatedAt)
    }

    companion object CREATOR : Parcelable.Creator<Shop> {
        override fun createFromParcel(parcel: Parcel): Shop {
            return Shop(parcel)
        }

        override fun newArray(size: Int): Array<Shop?> {
            return arrayOfNulls(size)
        }
    }
}