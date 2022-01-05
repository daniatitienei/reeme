package com.atitienei_daniel.reeme.domain.repository

import kotlinx.coroutines.flow.Flow

interface StoreCategoriesRepository {
    suspend fun insertCategory(categories: MutableList<String>)

    val getCategories: Flow<MutableList<String>?>
}