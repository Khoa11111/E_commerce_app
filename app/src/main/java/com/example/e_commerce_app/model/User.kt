package com.example.e_commerce_app.model

import android.os.Parcel
import android.os.Parcelable

data class User(
    val id: Int?,
    val Name: String?,
    val Passwords: String?,
    val email: String,
    val SDT: String?,
    val Address: String?,
    val imgUS: String?,
    val thongTinThanhToan: String?,
    val role: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        id?.let { dest.writeInt(it) }
        dest.writeString(Name)
        dest.writeString(Passwords)
        dest.writeString(email)
        dest.writeString(SDT)
        dest.writeString(Address)
        dest.writeString(imgUS)
        dest.writeString(thongTinThanhToan)
        dest.writeString(role)
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
