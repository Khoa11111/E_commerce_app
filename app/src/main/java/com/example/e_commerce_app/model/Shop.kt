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
    val createdAt: String,
    val updatedAt: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(shop_name)
        parcel.writeString(Address)
        parcel.writeInt(id_user)
        parcel.writeString(status)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
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