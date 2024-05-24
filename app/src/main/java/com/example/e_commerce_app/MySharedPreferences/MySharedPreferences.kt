package com.example.e_commerce_app.MySharedPreferences

import android.content.Context

class MySharedPreferences(private val mContext: Context) {

    private val MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES"

    fun putStringValue(key: String, value: String) {
        val sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getStringValue(key: String): String? {
        val sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, "not found")
    }

    fun putIntValue(key: String, value: Int) {
        val sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getIntValue(key: String): Int {
        val sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(key, -1)
    }

    fun clearMySharedPreferences() {
        val sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }
}
