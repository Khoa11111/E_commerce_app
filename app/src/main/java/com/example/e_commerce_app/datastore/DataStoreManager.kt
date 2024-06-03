package com.example.e_commerce_app.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.e_commerce_app.model.Category
import com.example.e_commerce_app.model.Product
import com.example.e_commerce_app.model.Shop
import com.example.e_commerce_app.model.User
import kotlinx.coroutines.flow.map

class DataStoreManager(context: Context) {
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore("E_COMERRCE_APP")
    private val mDataStore = context.datastore

    companion object {
        val idCurrentUser = intPreferencesKey("ID_CURRENT_USER")
        val emailCurrentUser = stringPreferencesKey("EMAIL_CURRENT_USER")
        val roleCurrentUser = stringPreferencesKey("ROLE_CURRENT_USER")
        val idShopCurrentUSer = intPreferencesKey("ID_SHOP_CURRENT_USER")
        val nameShopCurrentUser = stringPreferencesKey("NAME_SHOP_CURRENT_USER")
        val addressShopCurrentUser = stringPreferencesKey("ADDRESS_SHOP_CURRENT_USER")

        val idCurrentProduct = intPreferencesKey("ID_CURRENT_PRODUCT")
        val NameCurrentProduct = stringPreferencesKey("NAME_CURRENT_PRODUCT")
        val PriceCurrentProduct = intPreferencesKey("PRICE_CURRENT_PRODUCT")
        val idCategoryCurrent = intPreferencesKey("CATEGORY_CURRENT_PRODUCT")

        val idCategorySelected = intPreferencesKey("ID_CATEGORY_SELECTED")
    }

    // store user's attributes needed
    suspend fun storeCurrenUser(user: User) {
        mDataStore.edit { pref ->
            pref[idCurrentUser] = user.id!!
            pref[emailCurrentUser] = user.email
            pref[roleCurrentUser] = user.role!!

        }
    }

    suspend fun getCurrentUser() = mDataStore.data.map { pref ->
        User(
            pref[idCurrentUser],
            null,
            null,
            pref[emailCurrentUser]!!,
            null,
            null,
            null,
            null,
            pref[roleCurrentUser],
            null
        )
    }

    suspend fun storeCurrentID(product: Product) {
        mDataStore.edit { pref ->
            pref[idCurrentProduct] = product.id!!
            pref[NameCurrentProduct] = product.product_name
            pref[PriceCurrentProduct] = product.product_price
        }
    }

    suspend fun getCurrentID() = mDataStore.data.map { pref ->
        pref[idCurrentProduct]?.let {
            pref[NameCurrentProduct]?.let { it1 ->
                pref[PriceCurrentProduct]?.let { it2 ->
                    Product(
                        it,
                        it1,
                        null.toString(),
                        0,
                        null.toString(),
                        0,
                        0,
                        it2,
                        null.toString(),
                        null,
                        null,
                        null.toString(),
                        null.toString(),
                        null,
                        null
                    )
                }
            }
        }
    }

    suspend fun storeCurrentIDCategory(id: Int) {
        mDataStore.edit { pref ->
            pref[idCategoryCurrent] = id
        }
    }

    suspend fun getCurrentIDCategory() = mDataStore.data.map { pref ->
        pref[idCategoryCurrent]
    }

    suspend fun storeShop(shop: Shop) {
        mDataStore.edit { pref ->
            pref[idShopCurrentUSer] = shop!!.id
            pref[nameShopCurrentUser] = shop.shop_name!!
            pref[addressShopCurrentUser] = shop.Address!!
        }
    }

    suspend fun getShop() = mDataStore.data.map { pref ->
        pref[idShopCurrentUSer]?.let {
            pref[idCurrentUser]?.let { it1 ->
                Shop(
                    it,
                    pref[nameShopCurrentUser],
                    null,
                    pref[addressShopCurrentUser],
                    it1,
                    null,
                    null,
                    null
                )
            }
        }
    }

    suspend fun storeIdCategory(id: Int) {
        mDataStore.edit { pref ->
            pref[idCategorySelected] = id
        }
    }

    suspend fun getIdCategory() = mDataStore.data.map { pref ->
        pref[idCategorySelected]
    }
}