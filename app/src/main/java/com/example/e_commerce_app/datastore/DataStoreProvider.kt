package com.example.e_commerce_app.datastore

import android.content.Context

object DataStoreProvider {
    private lateinit var instance: DataStoreManager

    fun getInstance(context: Context): DataStoreManager {
        if (!::instance.isInitialized) {
            instance = DataStoreManager(context.applicationContext)
        }
        return instance
    }
}