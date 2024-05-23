package com.example.e_commerce_app.util

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

object Utils {
    val USER_BASE_URL = "http://192.168.2.17:5000/"

    fun encodeUriToBase64(uri: Uri?, context: Context): String {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bytes = stream.toByteArray()
        val encodedBase64 = Base64.encodeToString(bytes, Base64.DEFAULT)
        return encodedBase64
    }


    fun decodeBase64ToBitmap(base64String: String): Bitmap {
        val bytes = Base64.decode(base64String, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        return bitmap
    }

}