package com.atitienei_daniel.reeme.presentation.ui.screens.reminders

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.atitienei_daniel.reeme.domain.model.Reminder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RemindersViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _state = mutableStateOf<List<Reminder>>(listOf())
    val state: State<List<Reminder>> = _state

    init {
        getReminders()
    }

    private fun getReminders() {
        firestore.collection("users/${auth.currentUser?.uid!!}/reminders")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w(TAG, "Listen failed.", error)
                    return@addSnapshotListener
                }

                _state.value = value!!.toObjects()
            }
    }
}