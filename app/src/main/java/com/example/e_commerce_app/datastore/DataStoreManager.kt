package com.example.e_commerce_app.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(context: Context) {
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore("user")
    private val mDataStore = context.datastore

    companion object {
        val id = intPreferencesKey("iduser")
    }

    suspend fun storeId(value: Int) {
        mDataStore.edit { pref ->
            pref[id] = value
        }
    }

    val idFlow: Flow<Int> = mDataStore.data.map { pref ->
        pref[id] ?: 0
    }
}