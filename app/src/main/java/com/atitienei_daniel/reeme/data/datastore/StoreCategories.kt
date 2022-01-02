package com.atitienei_daniel.reeme.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreCategories(
    private val context: Context
) {

    companion object {
        private val Context.datastore: DataStore<Preferences> by preferencesDataStore("reminderCategories")
        val CATEGORIES_KEY = stringPreferencesKey("categories")
    }

    val getCategories: Flow<List<String>?> = context.datastore.data
        .map { preferences ->
            decode(preferences[CATEGORIES_KEY] ?: "")
        }
    
    suspend fun insertCategory(categories: List<String>) {
        context.datastore.edit { preferences -> 
            preferences[CATEGORIES_KEY] = encode(categories)
        }
    }

    private fun decode(databaseValue: String) =
        if (databaseValue.isEmpty()) {
            listOf()
        } else {
            databaseValue.split(",")
        }

    private fun encode(value: List<String>) = value.joinToString(separator = ",")
}