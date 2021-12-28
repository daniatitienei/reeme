package com.atitienei_daniel.reeme.domain.model

data class User(
    val email: String = "",
    val name: String = "",
    val categories: List<String> = emptyList()
)
