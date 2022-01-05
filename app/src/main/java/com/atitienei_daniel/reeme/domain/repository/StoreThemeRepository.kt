package com.atitienei_daniel.reeme.domain.repository

import com.atitienei_daniel.reeme.ui.utils.enums.Theme
import kotlinx.coroutines.flow.Flow

interface StoreThemeRepository {
    val getTheme: Flow<Theme>

    suspend fun setTheme(theme: Theme)
}