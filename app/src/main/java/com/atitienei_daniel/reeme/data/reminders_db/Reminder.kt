package com.atitienei_daniel.reeme.data.reminders_db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reminder(
    @PrimaryKey val id: Int? = null,
    val title: String,
    val description: String?,
    val isPinned: Boolean = false,
    val repeat: Int = 0,
    val isDone: Boolean = false,
    val color: Int,
)
