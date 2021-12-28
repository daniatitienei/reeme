package com.atitienei_daniel.reeme.domain.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reminder(
    val title: String = "",
    val description: String? = "",
    val pinned: Boolean = false,
    val repeat: Int = 0,
    val timestamp: Timestamp = Timestamp.now(),
    val color: String = "",
    val categories: List<String> = emptyList()
) : Parcelable