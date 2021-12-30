package com.atitienei_daniel.reeme.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reminder(
    val title: String = "",
    val description: String? = "",
    val isPinned: Boolean = false,
    val repeat: Int = 0,
    val isDone: Boolean = false,
    val color: Int = 0,
    val categories: List<String> = emptyList(),
) : Parcelable