package com.atitienei_daniel.reeme.data.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreCategories(
    private val context: Context
) {

    companion object {
        private val Context.datastore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(
            "reminderCategories"
        )
        val CATEGORIES_KEY = stringPreferencesKey("categories")
    }

    val getCategories: Flow<MutableList<String>?> = context.datastore.data
        .map { preferences ->
            return@map decode(databaseValue = preferences[CATEGORIES_KEY] ?: "")
        }

    suspend fun insertCategory(categories: MutableList<String>) {
        context.datastore.edit { preferences ->
            preferences[CATEGORIES_KEY] = encode(categories)
        }
    }

    private fun decode(databaseValue: String): MutableList<String> =
        if (databaseValue.isEmpty()) {
            mutableListOf()
        } else {
            databaseValue.split(",").toMutableList()
        }

    private fun encode(value: MutableList<String>): String = value.joinToString(separator = ",")
}