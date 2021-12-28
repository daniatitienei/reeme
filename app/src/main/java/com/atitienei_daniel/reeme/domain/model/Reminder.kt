package com.atitienei_daniel.reeme.domain.model

import com.google.firebase.Timestamp

data class Reminder(
    val title: String = "",
    val description: String?,
    val pinned: Boolean = false,
    val repeat: Int = 0,
    val timestamp: Timestamp,
    val color: Int = 0,
    val categories: List<String> = emptyList()
)