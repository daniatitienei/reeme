package com.atitienei_daniel.reeme.presentation.ui.screens.create_reminder

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.atitienei_daniel.reeme.domain.model.Reminder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateReminderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private var _titleError = mutableStateOf<String?>(null)
    val titleError: State<String?> = _titleError

    fun createReminder(reminder: Reminder) {
        if (reminder.title.isEmpty()) {
            _titleError.value = "Title must be completed."
            return
        }

        firestore.collection("users/${auth.currentUser?.uid}/reminders")
            .add(
                hashMapOf(
                    "title" to reminder.title,
                    "description" to reminder.description,
                    "color" to reminder.color,
                    "categories" to reminder.categories,
                    "repeat" to reminder.repeat,
                    "pinned" to reminder.pinned,
                    "timestamp" to reminder.timestamp
                )
            )
            .addOnSuccessListener { Log.d("CreateReminder", "SUCCESS") }
            .addOnFailureListener { e -> Log.d("CreateReminder", "FAILED, $e") }
    }
}
