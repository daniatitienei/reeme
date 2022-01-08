package com.atitienei_daniel.reeme.data.repository.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.atitienei_daniel.reeme.domain.repository.StoreThemeRepository
import com.atitienei_daniel.reeme.ui.utils.enums.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreThemeRepositoryImpl(
    private val context: Context,
) : StoreThemeRepository {

    companion object {
        private val Context.datastore: DataStore<Preferences> by preferencesDataStore(
            "themeDatastore"
        )
        val THEME_KEY = intPreferencesKey("theme")
    }

    override val getTheme: Flow<Theme>
        get() = context.datastore.data.map { preferences ->
            when (preferences[THEME_KEY]) {
                2 -> Theme.DARK
                1 -> Theme.LIGHT
                else -> Theme.AUTO
            }
        }

    override suspend fun setTheme(theme: Theme) {
        context.datastore.edit { preferences ->
            preferences[THEME_KEY] = theme.ordinal
        }
    }
}