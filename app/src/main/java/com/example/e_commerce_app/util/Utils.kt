package com.example.e_commerce_app.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import coil.request.Tags
import java.io.ByteArrayOutputStream

object Utils {
    val USER_BASE_URL = "https://e7cc-14-165-200-6.ngrok-free.app/"
    val TAG = "CheckProblem"

    fun encodeUriToBase64(uri: Uri?, context: Context): String {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bytes = stream.toByteArray()
        val encodedBase64 = "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.DEFAULT)
        return encodedBase64
    }

    fun extractPrefix(dataString: String): String {
        val indexOfComma = dataString.indexOf(",")
        if (indexOfComma != -1) {
            return dataString.substring(indexOfComma + 1)
        } else {
            return dataString
        }
    }

    fun decodeBase64ToBitmap(base64String: String): Bitmap {
        val bytes = Base64.decode(base64String, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        return bitmap
    }

}