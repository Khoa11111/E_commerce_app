package com.example.e_commerce_app.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(context: Context) {
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore("E_COMERRCE_APP")
    private val mDataStore = context.datastore

    companion object {
        val idCurrentUser = intPreferencesKey("ID_CURRENT_USER")
        val emailCurrentUser = stringPreferencesKey("EMAIL_CURRENT_USER")
    }

    // store id of current user
    suspend fun storeIdCurrenUser(value: Int) {
        mDataStore.edit { pref ->
            pref[idCurrentUser] = value
        }
    }

    // get id of current user
    val idCurrenUserFlow: Flow<Int> = mDataStore.data.map { pref ->
        pref[idCurrentUser] ?: 0
    }

    // store email of current user
    suspend fun storeEmailCurrentUser(value: String) {
        mDataStore.edit { pref ->
            pref[emailCurrentUser] = value
        }
    }

    // get email of current user
    val emailCurrentUserFlow: Flow<String> = mDataStore.data.map { pref ->
        pref[emailCurrentUser] ?: ""
    }
}